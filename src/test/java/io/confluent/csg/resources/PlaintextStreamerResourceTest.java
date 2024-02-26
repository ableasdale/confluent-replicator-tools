package io.confluent.csg.resources;

import io.confluent.csg.FileProcessManager;
import io.confluent.csg.TestHelper;
import io.confluent.csg.providers.JerseyServer;
import io.confluent.csg.util.Utils;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.filter.EncodingFeature;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static jakarta.ws.rs.core.Response.Status.NOT_ACCEPTABLE;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaintextStreamerResourceTest extends JerseyTest {

    @BeforeAll
    public static void setup() {
        PropertiesConfiguration config = Utils.loadConfigurationFile();
        FileProcessManager fpm = new FileProcessManager();
        fpm.processLogFile(config.getString("logfile"));
    }

    @Override
    public Application configure() {
        return JerseyServer.getBaseResourceConfig();
    }

    @Test
    public void getLargeTxtFileAsChunkedWithDeflate() {

        // This DOES work: curl --compressed -v -o - http://localhost:9992/files/fn
        Client client = ClientBuilder.newClient(new ClientConfig());
        client.register(new EncodingFeature("deflate", DeflateEncoder.class));
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");

        WebTarget target = client.target(TestHelper.UNIT_TEST_JERSEY_URL);
        Response response = target.path("files").
                path("filename").
                request().
                accept(HttpHeaders.ACCEPT_ENCODING, "deflate").
                accept(MediaType.APPLICATION_OCTET_STREAM).
                get(Response.class);

        assertEquals("deflate", response.getHeaderString("Content-Encoding"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaderString("Content-Type"));
        assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        assertEquals(HTTP_OK, response.getStatus());
        response.close();
    }

    @Test
    public void getLargeTxtFileAsChunkedWithGzip() {

        Client client = ClientBuilder.newClient(new ClientConfig());
        client.register(new EncodingFeature("gzip", GZipEncoder.class));
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");

        WebTarget target = client.target(TestHelper.UNIT_TEST_JERSEY_URL);
        Response response = target.path("files").
                path("filename").
                request().
                accept(HttpHeaders.ACCEPT_ENCODING, "gzip").
                accept(MediaType.APPLICATION_OCTET_STREAM).
                get(Response.class);

        assertEquals("gzip", response.getHeaderString("Content-Encoding"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaderString("Content-Type"));
        assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        assertEquals(HTTP_OK, response.getStatus());
        response.close();
    }

    /**
     * Accept header for request doesn't match the endpoint, so the request will fail
     */
    @Test
    public void testUnacceptableTest() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
        WebTarget target = client.target("http://localhost:9998/");
        Response response = target.path("files").
                path("filename").
                request().
                accept(MediaType.TEXT_PLAIN).
                get(Response.class);
        assertEquals(NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        response.close();
    }
}
