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
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogResourceTest extends JerseyTest {

    @BeforeAll
    public static void setup() {
        PropertiesConfiguration config = Utils.loadConfigurationFile();
        FileProcessManager fpm = new FileProcessManager();
        fpm.processLogFile(config.getString("logfile"));
    }

    @Override
    public Application configure() {
        // enable(TestProperties.LOG_TRAFFIC);
        // enable(TestProperties.DUMP_ENTITY);
        return JerseyServer.getBaseResourceConfig();
    }

    @Test
    public void getLargeTxtFileAsChunkedWithDeflate() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        client.register(new EncodingFeature("deflate", DeflateEncoder.class));
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");

        WebTarget target = client.target(TestHelper.UNIT_TEST_JERSEY_URL);
        Response response = target.path("logs").
                path("workerTask").
                request().
                accept(HttpHeaders.ACCEPT_ENCODING, "deflate").
                accept(MediaType.TEXT_HTML).
                get(Response.class);

        assertEquals("deflate", response.getHeaderString("Content-Encoding"));
        assertEquals(MediaType.TEXT_HTML+";charset=UTF-8", response.getHeaderString("Content-Type"));
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
        Response response = target.path("logs").
                path("workerTask").
                request().
                accept(HttpHeaders.ACCEPT_ENCODING, "gzip").
                accept(MediaType.TEXT_HTML).
                get(Response.class);

        assertEquals("gzip", response.getHeaderString("Content-Encoding"));
        assertEquals(MediaType.TEXT_HTML+";charset=UTF-8", response.getHeaderString("Content-Type"));
        assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        assertEquals(HTTP_OK, response.getStatus());
        response.close();
    }
}
