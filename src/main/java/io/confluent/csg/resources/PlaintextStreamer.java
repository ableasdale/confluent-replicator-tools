package io.confluent.csg.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/files")
public class PlaintextStreamer extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)

    public StreamingOutput getFile(@PathParam("fileName") final String fileName)  throws Exception  {
        //create instance of StreamingOutput here
        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try
                {
                    java.nio.file.Path path = Paths.get("C:/temp/test.pdf");
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                }
                catch (Exception e)
                {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        return streamingOutput;
    }
    /*
    return Response
	            .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	            .header("content-disposition","attachment; filename = myfile.pdf")
	            .build();
	            https://howtodoinjava.com/jersey/jersey-streamingoutput/
     */
}
