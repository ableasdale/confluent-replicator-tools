package io.confluent.csg.resources;

import io.confluent.csg.providers.LogDataProvider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());



    @Context
    protected UriInfo uriInfo;

    /**
     * Attempt to make sure input is an integer.
     *
     * @param text the value passed to the method from the resource (a URI
     *             segment)
     * @return true or false
     */
    protected boolean canBeParsedAsInteger(String text) {
        try {
            Integer.valueOf(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Freemarker template parameter map for the HTTP Exception Page template
     * (exception.ftl)
     *
     * @param statusCode
     * @param message
     * @return
     */
    protected Map<String, Object> createExceptionModel(int statusCode, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", String.valueOf(statusCode));
        map.put("message", message);
        return map;
    }

    protected Map<String, Object> createModel() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Dashboard and overview");
        map.put("configs", LogDataProvider.getInstance().getConfigs());
        map.put("logs", LogDataProvider.getInstance().getLogs());
        map.put("logsizes", LogDataProvider.getInstance().getLogSizeMap());
        map.put("filename", LogDataProvider.getInstance().getFilename());
        List list = (List) LogDataProvider.getInstance().getLogs().get("workerTask");
        map.put("bigfile", String.join(", ", list));
        return map;
    }


    protected Map<String, Object> createModel(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("title", "Dashboard and overview");
        map.put("configs", LogDataProvider.getConfigs());
        return map;
    }

    public URI getUri(Class c) {
        return uriInfo.getBaseUriBuilder().path(c).build();
    }

    /**
     * General handler for exceptions in the request made to the resource This
     * is how you do a custom Server Exception with the Freemarker template
     *
     * @param message
     * @return
     */
    protected Response wrapViewableExceptionResponse(String message) {
        LOG.error(MessageFormat.format("Exception encountered: {0}", message));
        return Response
                .status(500)
                .entity(new Viewable("/exception", createExceptionModel(500,
                        message))).build();
    }

    /**
     * Wraps an 'OK' response with the Viewable (freemarker) template
     *
     * @param templateName
     * @param model
     * @return
     */
    protected Response wrapViewableResponse(String templateName,
                                            Map<String, Object> model) {
        return Response.status(Response.Status.OK)
                .entity(new Viewable(templateName, model)).build();
    }
}