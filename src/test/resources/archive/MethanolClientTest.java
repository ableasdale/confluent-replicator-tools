import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MoreBodyHandlers;
import com.github.mizosoft.methanol.MutableRequest;
import org.glassfish.grizzly.http.HttpHeader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class MethanolClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Test
    public void testMethanolClientGzip() throws IOException, InterruptedException {
        var client = Methanol.create();
        var request = MutableRequest.GET("http://localhost:9992/files/filename").header("Accept-Encoding","gzip");
        //request.setHeader("Accept-Encoding","gzip");
        //var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //MoreBodyHandlers.decoding(BodyHandlers.ofString())
        var response = client.send(request, MoreBodyHandlers.decoding(HttpResponse.BodyHandlers.ofString()));

        //for (Object o : response.headers().map().forEach((name, values) -> values.forEach(value -> LOG.info(""+name + ':' + value)));
        for (Map.Entry<?, ?> entry  : response.headers().map().entrySet()){
            LOG.info(entry.getKey() + " | " + entry.getValue());
        }

    }
}
