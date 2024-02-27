package io.confluent.csg.providers;

import com.google.common.collect.ImmutableSet;
import io.confluent.csg.util.Consts;
import io.confluent.csg.util.Utils;
import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.CompressionConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;

public class JerseyServer extends Thread {

    public static final URI BASE_URI = getBaseURI();
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static URI getBaseURI() {
        return UriBuilder.fromUri(Consts.URI_BASE).port(Consts.GRIZZLY_HTTP_PORT).build();
    }

    public static ResourceConfig getBaseResourceConfig() {
        return new ResourceConfig()
                .property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true")
                .property(ServerProperties.LOCATION_HEADER_RELATIVE_URI_RESOLUTION_RFC7231, Boolean.TRUE)
                .property(ServerProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, 32768)
                // Project specific packages
                .packages(Consts.RESOURCE_PACKAGES)
                // Upload Handler
                //.register(MultiPartFeature.class)
                /* MVC (Template) Engines */
                .register(org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature.class)
                .register(EncodingFilter.class)
                .register(GZipEncoder.class)
                .register(DeflateEncoder.class)
                .property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH, Consts.FREEMARKER_TEMPLATE_PATH);
    }

    @Override
    public void run() {
        try {
            LOG.info(String.format("Starting HTTP Server on port %d - use CTRL^C to stop the server", Consts.GRIZZLY_HTTP_PORT));
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, getBaseResourceConfig());
            final StaticHttpHandler staticHttpHandler = new StaticHttpHandler(Consts.STATIC_RESOURCE_DIRECTORY_ROOT);
            server.getServerConfiguration().addHttpHandler(staticHttpHandler, Consts.ASSETS_DIRECTORY_PATH);
            // Configure GZip Compression
            // Test with: curl --compressed -v -o - http://localhost:9992/logs/workerTask
            // curl -H "Accept-Encoding: gzip, deflate" http://localhost:9992/logs/workerTask
            /*
< HTTP/1.1 200 OK
< Vary: Accept-Encoding
< Content-Encoding: deflate
< Content-Type: text/html;charset=UTF-8
< Transfer-Encoding: chunked
             */
            NetworkListener nl = server.getListener("grizzly");
            final CompressionConfig compressionConfig = nl.getCompressionConfig();
            compressionConfig.setCompressionMode(CompressionConfig.CompressionMode.ON); // the mode
            compressionConfig.setCompressionMinSize(1); // the min amount of bytes to compress
            compressionConfig.setCompressibleMimeTypes(ImmutableSet.<String> builder()
                    .add("text/plain")
                    .add("text/html")
                    .build());
            server.start();
            synchronized (this) {
                //CTRL-C to stop the server
                while (true) {
                    this.wait();
                }
            }
        } catch (final Exception e) {
            Utils.returnExceptionString(e);
        }
    }

}