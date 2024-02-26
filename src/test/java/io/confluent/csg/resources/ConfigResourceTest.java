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
import org.glassfish.grizzly.utils.Charsets;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigResourceTest extends JerseyTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    public void basicJerseyServiceTest() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(TestHelper.UNIT_TEST_JERSEY_URL);
        Response response = target.path("configs").
                path("TaskConfig").
                request().
                header(HttpHeaders.ACCEPT,MediaType.TEXT_HTML).
                header(HttpHeaders.ACCEPT_CHARSET, Charsets.UTF8_CHARSET).
                //header().
                //assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
               // accept(MediaType.TEXT_HTML).//HttpHeaders.ACCEPT_CHARSET
                //accept(MediaType.)
               //         accept(Charsets.UTF8_CHARSET).
                get(Response.class);

        //WebTarget webTarget
        //      = client.target("http://localhost:9992/");
        //WebResource r = client.resource(uri);
        // ClientResponse response = webTarget..type(MediaType.APPLICATION_XML).post(ClientResponse.class, request);
        //System.out.println(response.getStatus());
// or WebTarget employeeWebTarget
//  = webTarget.path("resources/employees");
        //Invocation.Builder invocationBuilder = webTarget.request().get(String.class);
        // = employeeWebTarget.request(MediaType.APPLICATION_JSON);

        /*
        Client client = Client.create();
			WebResource r = client.resource(uri);
			ClientResponse response = r.type(MediaType.APPLICATION_XML).post(ClientResponse.class, request);
			System.out.println(response.getStatus());
         */

        for (String s: response.getHeaders().keySet()){
            LOG.debug("k"+s+" | "+response.getHeaderString(s));

        }
       // assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        // assertEquals("Vary", response.getHeaderString("Accept-Encoding"));
        assertEquals(MediaType.TEXT_HTML+";charset=UTF-8", response.getHeaderString(HttpHeaders.CONTENT_TYPE));
        assertEquals(HTTP_OK, response.getStatus());
    }
}
