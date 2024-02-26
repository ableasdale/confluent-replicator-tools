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
                .header("Accept", "application/octet-stream")
                .GET()
                .build();
        LOG.debug(MessageFormat.format("Request Headers: {0}", request.headers().toString()));
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        LOG.debug(MessageFormat.format("Response Headers: {0}", response.headers().toString()));
        assertEquals("gzip",response.headers().firstValue("Content-Encoding").get());
        //assertEquals("application/octet-stream",response.headers().firstValue("Content-Type").get());
        assertEquals("chunked",response.headers().firstValue("Transfer-Encoding").get());
        assertEquals(HTTP_OK, response.statusCode());

    }


   // @Test
    public void notSure(){
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
                .defaultHeader("Content-Type", "application/octet-stream")
        .defaultHeader("Accept-Encoding","gzip")
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
   // @Test
    public void aboutToGiveUp() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        try {
            HttpGet post = new HttpGet("http://localhost:9992/files/filename");


            //StringEntity entiy = new StringEntity(data, "UTF-8");
            //post.setEntity(entiy);
            post.setHeader("Content-Type", "application/octet-stream");
            post.setHeader("Accept-Encoding","gzip");
            CloseableHttpResponse response = client.execute(post);
            for(Header h : response.getHeaders()){
                LOG.info ("*"+h.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   //
   // @Test
    public void getLargeTxtFileAsChunkedWithDeflateTest() throws IOException, ProtocolException {
        LOG.info("test");
        //DecompressingHttpClient
        CloseableHttpClient client = HttpClientBuilder.create().build();
        //HttpResponse response = httpClient.execute(new HttpGet("http://localhost:9992/files/filename"));
               // response -> {
                    //handle response
                 //   String bodyAsString = EntityUtils.toString(response.getEntity());
                    //assertThat(bodyAsString, notNullValue());


                   // LOG.info(bodyAsString);
                  //  return response;
               // }
       // );
//        CloseableHttpClient client = HttpClients.custom()
//                .disableContentCompression()
//                .build();

        HttpGet request = new HttpGet("http://localhost:9992/files/filename");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "deflate");
        request.addHeader(HttpHeaders.ACCEPT, "application/xml");
// Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
        CloseableHttpResponse response = client.execute(request);//, context);
        //HttpEntity entity = new GzipDecompressingEntity(new ByteArrayEntity(response.getBodyBytes(), ContentType.APPLICATION_OCTET_STREAM));
        //GzipDecompressingEntity entity = (GzipDecompressingEntity) response.getEntity();
       // Header contentEncodingHeader = entity.getContentEncoding();
        //assertEquals("deflate", response.getHeader("Content-Encoding"));
        assertEquals(ContentType.APPLICATION_OCTET_STREAM, response.getHeaders(HttpHeaders.CONTENT_TYPE));
                //response.getHeader("Content-Type"));
        assertEquals("chunked", response.getHeader("Transfer-Encoding"));
        assertEquals(HTTP_OK, response.getCode());
        //LOG.info(ContentType.parse(response.getEntity().getContentType()).getMimeType());


    }

    @Test
    public void testGzipAndChunkedEncodingWithHttpClient5() throws IOException, ProtocolException {

        CloseableHttpClient httpClient = HttpClients.custom()
                //.disableRedirectHandling()
                .disableContentCompression()
                .build();
        HttpGet request = new HttpGet("http://localhost:9992/files/filename");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

        HttpResponse response = httpClient.execute(request);

        for(Header h : response.getHeaders()){
            LOG.info ("*"+h.toString());
        }
        assertEquals("gzip",response.getHeader("Content-Encoding").getValue());
        assertEquals("application/octet-stream",response.getHeader("Content-Type").getValue());
        assertEquals("chunked",response.getHeader("Transfer-Encoding").getValue());
        assertEquals(HTTP_OK, response.getCode());

    }

}
   // public static void main(String[] args) throws IOException {

        /*
        httpClient.execute(new HttpGet("http://www.google.com"),
   response -> {
     assertThat(response.getCode()).isEqualTo(200);
     return response;
   }
);

        httpClient.execute(new HttpGet("http://www.google.com"),
                response -> {
                    final String contentMimeType = ContentType.parse(response.getEntity().getContentType()).getMimeType();
                    assertThat(contentMimeType).isEqualTo(ContentType.TEXT_HTML.getMimeType());
                    return response;
                }
        );
        httpClient.execute(new HttpGet("http://www.google.com"),
                response -> {
                    String bodyAsString = EntityUtils.toString(response.getEntity());
                    assertThat(bodyAsString, notNullValue());
                    return response;
                }
        );*/
   // }
/*
CloseableHttpClient client = HttpClients.custom()
    .disableContentCompression()
    .build();

HttpGet request = new HttpGet(urlSring);
request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");
 */
