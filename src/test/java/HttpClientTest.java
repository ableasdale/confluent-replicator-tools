import jakarta.ws.rs.core.MediaType;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.entity.GzipDecompressingEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Enumeration;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void testChunkedGzippedResponseFromServer() throws URISyntaxException, IOException, InterruptedException {
        // Using the plain JDK Java HTTP Client (java.net.http.HttpClient)
        java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                .version(java.net.http.HttpClient.Version.HTTP_2)
                .build();
        URI uri = new URI("http://localhost:9992/files/filename");
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
        HttpGet request = new HttpGet("http://localhost:9992/files/filename");
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
        HttpGet request = new HttpGet("http://localhost:9992/files/filename");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

        CloseableHttpResponse response = httpClient.execute(request);

        for (Header h : response.getHeaders()) {
            LOG.info("*" + h.toString());
        }
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