import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JerseyClientLiveTest {

    public static final int HTTP_CREATED = 201;
    public static final int HTTP_OK = 200;
    //private RestClient client = new RestClient();

    @Test
    public void givenCorrectObject_whenCorrectJsonRequest_thenResponseCodeCreated() {
        //Employee emp = new Employee(6, "Johny");

        //Response response = client.createJsonEmployee(emp);

        //Client client = ClientBuilder.newClient();

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
}
