package io.confluent.csg;

import io.confluent.csg.providers.JerseyServer;
import io.confluent.csg.util.Utils;
import jakarta.ws.rs.core.Application;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.message.StatusLine;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@TestInstance(Lifecycle.PER_CLASS)
public class HttpClientTest extends JerseyTest {

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
    public void testChunkedGzippedResponseFromServer() throws URISyntaxException, IOException, InterruptedException {
        // Using the plain JDK Java HTTP Client (java.net.http.HttpClient)
        java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                .version(java.net.http.HttpClient.Version.HTTP_2)
                .build();
        URI uri = new URI(TestHelper.UNIT_TEST_JERSEY_URL+"/files/filename");
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept-Encoding", "gzip")
                .header("Accept", ContentType.APPLICATION_OCTET_STREAM.toString())
                .GET()
                .build();
        LOG.debug(MessageFormat.format("Request Headers: {0}", request.headers().toString()));
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        LOG.debug(MessageFormat.format("Response Headers: {0}", response.headers().toString()));
        assertEquals("gzip", response.headers().firstValue("Content-Encoding").get());
        //assertEquals("application/octet-stream",response.headers().firstValue("Content-Type").get());
        assertEquals("chunked", response.headers().firstValue("Transfer-Encoding").get());
        assertEquals(HTTP_OK, response.statusCode());

    }


    // @Test
    public void notSure() {
        // HttpClient httpClient = HttpClient.create().compress(true).wiretap(true);
//        HttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(cm)
//                .disableContentCompression() //this disables compression
//                .build();
        WebClient client = WebClient.builder()
                //.compress(true)
                .baseUrl("http://localhost:9992/files/filename")
                // .defaultCookie("cookieKey", "cookieValue")
                // .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString())
                .defaultHeader("Accept-Encoding", "gzip")
                //.clientConnector(new ReactorClientHttpConnector(client))
                //.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();
        // WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.get();

        /**
         * .get()
         * .uri(uri)
         * .accept(MediaType.APPLICATION_OCTET_STREAM)
         * .retrieve()
         * .bodyToFlux(byte[].class)
         */
    }

    // curl -X GET http://localhost:9992/files/f -H "Accept: application/octet-stream" -I

    @Test
    public void getLargeTxtFileAsChunkedWithDeflateTest() throws IOException, ProtocolException {

        CloseableHttpClient client = HttpClientBuilder.create().disableContentCompression().build();
        HttpGet request = new HttpGet(TestHelper.UNIT_TEST_JERSEY_URL+"/files/filename");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "deflate");
        request.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_OCTET_STREAM.toString());

        CloseableHttpResponse response = client.execute(request);//, context);
        //HttpEntity entity = new GzipDecompressingEntity(new ByteArrayEntity(response.getBodyBytes(), ContentType.APPLICATION_OCTET_STREAM));
        //GzipDecompressingEntity entity = (GzipDecompressingEntity) response.getEntity();
        // Header contentEncodingHeader = entity.getContentEncoding();
        assertEquals("deflate", response.getHeader("Content-Encoding").getValue());
        assertEquals(ContentType.APPLICATION_OCTET_STREAM.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE).getValue());
        assertEquals("chunked", response.getHeader("Transfer-Encoding").getValue());
        assertEquals(HTTP_OK, response.getCode());
        response.close();
    }

    @Test
    public void testGzipAndChunkedEncodingWithHttpClient5() throws IOException, ProtocolException {

        CloseableHttpClient httpClient = HttpClients.custom()
                //.disableRedirectHandling()
                .disableContentCompression()
                .build();
        HttpGet request = new HttpGet(TestHelper.UNIT_TEST_JERSEY_URL+"/files/filename");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

        CloseableHttpResponse response = httpClient.execute(request);

        // TODO - attempted to fix the deprecation messages... :(
        //HttpResponse response =  httpClient.execute( request );
               // .connectTimeout(MILLIS_ONE_SECOND)
               // .socketTimeout(MILLIS_ONE_SECOND)
               // .execute();
      // HttpResponse httpResponse = response.returnResponse();
        // StatusLine statusLine = httpResponse.getStatusLine();

        //HttpClientResponseHandler<String> response = new BasicHttpClientResponseHandler();
        //response.
        //final String responseBody = httpClient.execute(request, response);
        // CloseableHttpResponse response = httpClient.execute(request);
/*
        for (Header h : responseBody.getHeaders()) {
            LOG.debug("*" + h.toString());
        } */
        assertEquals("gzip", response.getHeader("Content-Encoding").getValue());
        assertEquals(ContentType.APPLICATION_OCTET_STREAM.toString(), response.getHeader("Content-Type").getValue());
        assertEquals("chunked", response.getHeader("Transfer-Encoding").getValue());
        assertEquals(HTTP_OK, response.getCode());
        response.close();
    }
}

/**
 * CloseableHttpClient httpclient = HttpClients.createDefault();
 * HttpGet httpGet = new HttpGet("http://targethost/homepage");
 * CloseableHttpResponse response1 = httpclient.execute(httpGet);
 *
 * try {
 *     System.out.println(response1.getStatusLine());
 *     HttpEntity entity1 = response1.getEntity();
 *     EntityUtils.consume(entity1);
 * } finally {
 *     response1.close();
 * }
 */