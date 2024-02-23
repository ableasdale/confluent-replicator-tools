package io.confluent.csg.resources;

import io.confluent.csg.providers.LogDataProvider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
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
//        map.put("ccloud", CCloudMetadataProvider.getConfluentCliVersion());
//        map.put("kcat", CCloudMetadataProvider.getKCatVersion());
        map.put("configs", LogDataProvider.getConfigs());
        map.put("logs", LogDataProvider.getLogs());
//        map.put("env_id", CCloudMetadataProvider.getCurrentEnvironment());
//        map.put("cluster_id", CCloudMetadataProvider.getCurrentKafkaCluster());
//        map.put("kafka_clusters", CCloudMetadataProvider.getKafkaClusters(false));
//        map.put("kafka_clusters_nav", CCloudMetadataProvider.getKafkaClusterMap(false));
//        map.put("system_info", Consts.SYSTEM_INFORMATION);
//        map.put("java_info", Consts.JAVA_INFORMATION);
        // map.put("path", PropertiesMap.getInstance().get("path"));
        return map;
    }

    protected Map<String, Object> createModel(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("title", "Dashboard and overview");
//        map.put("ccloud", CCloudMetadataProvider.getConfluentCliVersion());
//        map.put("kcat", CCloudMetadataProvider.getKCatVersion());
        map.put("configs", LogDataProvider.getConfigs());
//        map.put("env_id", CCloudMetadataProvider.getCurrentEnvironment());
//        map.put("cluster_id", CCloudMetadataProvider.getCurrentKafkaCluster());
//        map.put("kafka_clusters", CCloudMetadataProvider.getKafkaClusters(false));
//        map.put("kafka_clusters_nav", CCloudMetadataProvider.getKafkaClusterMap(false));
//        map.put("system_info", Consts.SYSTEM_INFORMATION);
//        map.put("java_info", Consts.JAVA_INFORMATION);
        // map.put("path", PropertiesMap.getInstance().get("path"));
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