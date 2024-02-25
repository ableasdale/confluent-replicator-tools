import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.filter.EncodingFeature;
import org.glassfish.jersey.message.DeflateEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javassist.util.proxy.FactoryHelper.writeFile;

public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void writeFile(InputStream fileInputStream, OutputStream outputStream) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }
    public static void main(String[] args) throws IOException {

        /* works
        java.nio.file.Path path = Paths.get("/Users/ableasdale/Downloads/connect-distributed.log");
        byte[] data = Files.readAllBytes(path);
        String content = new String(data);

        // print contents
        System.out.println(content);
        //output.write(data);
        //output.flush();

         */


        // This reads the entire request BUT it also logs the same exception!
        // Note that curl doesn't: curl --compressed -v -o - http://localhost:9992/files/filename
        // Next step - try another client library!

        Client client = ClientBuilder.newClient(new ClientConfig());
        client.register(new EncodingFeature("deflate", DeflateEncoder.class));
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");

        WebTarget target = client.target("http://localhost:9992/files/filename");
        Response response = target.
                request().
                accept(HttpHeaders.ACCEPT_ENCODING, "deflate").
                accept(MediaType.APPLICATION_OCTET_STREAM).
                get(Response.class);

        //OutputStream fileOutputStream = new FileOutputStream(outFile);
        InputStream fileInputStream = target.request().get(InputStream.class);
        //writeFile(fileInputStream, fileOutputStream);
        //LOG.info(response.bufferEntity());
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = reader.readLine();

        while (line != null) {
            line = reader.readLine();
            LOG.info(line);
        }
        reader.close();
    }
}
