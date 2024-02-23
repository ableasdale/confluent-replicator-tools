package io.confluent.csg.resources;

import io.confluent.csg.providers.LogDataProvider;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Path("/logs")
public class Log extends BaseResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @POST
    @Path("/{logfile}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable doPost(@PathParam("logfile") String name) {
        return getLogFile(name);
    }

    @GET
    @Path("/{logfile}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable getLogFile(@PathParam("logfile") String name) {
        LOG.debug("getLogFile() :: Rendering view");
        Map view = createModel(name); // TODO - add it here?
        view.put("file", LogDataProvider.getLogs().get(name));
        view.put("toast_heading", "Log View");
        view.put("toast_notification", "Viewing Logs for <strong>"+name+"</strong>");
        return new Viewable("/log", view);
    }
}
