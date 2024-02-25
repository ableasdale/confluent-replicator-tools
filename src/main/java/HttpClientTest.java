import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class HttpClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        httpClient.execute(new HttpGet("http://localhost:9992/files/filename"),
                response -> {
                    //handle response
                    String bodyAsString = EntityUtils.toString(response.getEntity());
                    //assertThat(bodyAsString, notNullValue());
                    LOG.info(bodyAsString);
                    return response;
                }
        );
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
    }
}
