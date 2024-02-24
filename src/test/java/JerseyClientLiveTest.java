import io.confluent.csg.Server;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.compression.zip.GZipEncoder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.filter.EncodingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JerseyClientLiveTest {

    public static final int HTTP_CREATED = 201;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int HTTP_OK = 200;

   // @BeforeAll
    public void startJersey() {
        // Jersey needs to be running for these tests to pass.
        // TODO - break up Server class so it can be started with a test file
    }

    @Test
    public void basicJerseyServiceTest() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target("http://localhost:9992/");
        Response response = target.path("configs").
                path("TaskConfig").
                request().
                accept(MediaType.TEXT_HTML).
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

        assertEquals(HTTP_OK, response.getStatus());
    }

    /*
    The endpoint is working:

  curl --compressed -v -o - http://localhost:9992/files/filename
*   Trying [::1]:9992...
* Connected to localhost (::1) port 9992
> GET /files/filename HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/8.4.0

            > Accept-Encoding: deflate, gzip
>
< HTTP/1.1 200 OK
            < Vary: Accept-Encoding
            < Content-Encoding: deflate
            < Content-Type: application/octet-stream
            < Transfer-Encoding: chunked

     */
    @Test
    public void getLargeFileReq() {


        Client client = ClientBuilder.newClient(new ClientConfig());
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
        WebTarget target = client.target("http://localhost:9992/");
        Response response = target.path("files").
                register(EncodingFilter.class).
                register(GZipEncoder.class).
                path("filename").
                // TODO - this isn't quite right yet - the request needs to state that it accepts gzip for this test to work
                property(ClientProperties.USE_ENCODING, "gzip").
                accept(HttpHeaders.ACCEPT_ENCODING, "gzip").
                accept(MediaType.APPLICATION_OCTET_STREAM).
                get(Response.class);

        /* TODO - Something like this will be useful for downloads
        OutputStream fileOutputStream = new FileOutputStream(outFile);
        InputStream fileInputStream = target.request().get(InputStream.class);
        writeFile(fileInputStream, fileOutputStream); */
        //return target.toString();
        assertEquals("gzip", response.getHeaderString("Content-Encoding"));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaderString("Content-Type"));
        assertEquals("chunked", response.getHeaderString("Transfer-Encoding"));
        assertEquals(HTTP_OK, response.getStatus());
    }

    /**
     * Accept header for request doesn't match the endpoint, so the request will fail
     */
    @Test
    public void testUnacceptableTest() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
        WebTarget target = client.target("http://localhost:9992/");
        Response response = target.path("files").
                path("filename").
                request().
                accept(MediaType.TEXT_PLAIN).
                get(Response.class);
        assertEquals(NOT_ACCEPTABLE, response.getStatus());
    }
}
