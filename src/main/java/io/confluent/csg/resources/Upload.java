package io.confluent.csg.resources;

import io.confluent.csg.FileProcessManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Path("/upload")
public class Upload extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Map<String, Object> createModel() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Upload ErrorLog");
        return map;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable uploadPage() {
        return new Viewable("/upload", createModel());
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadHandler(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader
    ) {

        String filePath = contentDispositionHeader.getFileName();

        LOG.info(String.format("Handling the upload of a new ErrorLog / Messages file: %s", filePath));

        FileProcessManager fpm = new FileProcessManager();
        try {
            fpm.processLogFile(fileInputStream, filePath);
        } catch (IOException e) {
            LOG.error(MessageFormat.format("IO Exception caught processing file {0}: {1}", filePath, e.getMessage()));
        }
        return Response.ok().build();
    }

}
