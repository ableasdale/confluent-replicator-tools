package io.confluent.csg.resources;

import io.confluent.csg.providers.LogDataProvider;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Path("/configs")
public class Config extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @POST
    @Path("/{config}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable doPost(@PathParam("config") String name) {
        return getConfigFile(name);
    }

    @GET
    @Path("/{config}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable getConfigFile(@PathParam("config") String name) {
        LOG.debug("getConfigFile() :: Rendering view");
        Map view = createModel(name); // TODO - add it here?
        view.put("file", LogDataProvider.getConfigs().get(name));
        view.put("toast_heading", "Configuration");
        view.put("toast_notification", "Configuration for <strong>"+name+"</strong>");
        return new Viewable("/configs", view);
    }
}
