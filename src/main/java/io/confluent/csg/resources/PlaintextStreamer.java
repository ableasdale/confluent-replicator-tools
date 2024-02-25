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
import java.text.MessageFormat;

@Path("/files")
public class PlaintextStreamer extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)

    public StreamingOutput getFile(@PathParam("fileName") final String fileName) {
        LOG.info("retrieving file: "+fileName);
        // ArrayList to file??
        /*
        FileWriter writer = new FileWriter("output.txt");
        for(String str: arr) {
            writer.write(str + System.lineSeparator());
        }
        writer.close(); */
        /*
        StreamingOutput streamingOutput = output -> {
            try
            {
                //java.nio.file.Path path = Paths.get(fileName);
                java.nio.file.Path path = Paths.get("/Users/ableasdale/Downloads/connect-distributed.log");
                LOG.info("File: "+fileName);
                byte[] data = Files.readAllBytes(path);
                output.write(data);
                output.flush();
            }
            catch (Exception e)
            {
                throw new WebApplicationException(MessageFormat.format("File {0} not found", fileName));
            }
        };
        return streamingOutput; */

        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try
                {
                    // TODO - hard coded
                    java.nio.file.Path path = Paths.get("/Users/ableasdale/Downloads/connect-distributed.log");
                    // java.nio.file.Path path = Paths.get("/Users/ableasdale/Downloads/connect-distributed.log");
                    LOG.info("path"+path.toString());
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                }
                catch (Exception e)
                {
                    LOG.error("Error: ",e);
                    //throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        return streamingOutput;
    }
    /* For a downloadable item - we will set the Content-Disposition header with a filename
    return Response
	            .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	            .header("content-disposition","attachment; filename = myfile.pdf")
	            .build();
	            https://howtodoinjava.com/jersey/jersey-streamingoutput/
     */
}

// https://stackoverflow.com/questions/48768006/jersey-an-i-o-error-has-occurred-while-writing-a-response-message