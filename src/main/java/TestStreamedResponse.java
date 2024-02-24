
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.server.Uri;

import java.io.IOException;

import static org.glassfish.jersey.model.Parameter.Source.URI;

public class TestStreamedResponse {

    // TODO - move to JerseyTest in the future...
    public static void main(String[] args) {


    }

    public String getFileReq() throws IOException { // File outFile


        Client client = ClientBuilder.newClient(new ClientConfig());
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
        WebTarget target = client.target("http://localhost:9992/files/filename");

        /* Something like this will be useful for downloads
        OutputStream fileOutputStream = new FileOutputStream(outFile);
        InputStream fileInputStream = target.request().get(InputStream.class);
        writeFile(fileInputStream, fileOutputStream); */
        return target.toString();
    }

}
