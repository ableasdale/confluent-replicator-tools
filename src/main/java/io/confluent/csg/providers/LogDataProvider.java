package io.confluent.csg.providers;

import com.google.common.base.Splitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LogDataProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    //private static String SELECTED_ENVIRONMENT;
    //private static String SELECTED_KAFKA_CLUSTER;    private static String KCAT_VERSION = getKCatVersion();
    //private final String CONFLUENT_VERSION = Utils.runCmd("confluent --version");    public static String KAFKA_CLUSTER_JSON_LIST = getKafkaClusters(false);
    private static Map CONFIGS;

    //public static Map KAFKA_CLUSTER_LIST = getKafkaClusterMap(false);
    private LogDataProvider() {
        LOG.info("LogDataProvider :: instantiating");
    }

    /*
    public static Map getKafkaClusterMap(boolean modified) {
        if (KAFKA_CLUSTER_LIST == null || modified) {
            LOG.info("getKafkaClusterMap() called (modified)...");
            Map<String, String> kcMap = new TreeMap<String, String>();
            String currentCluster = Utils.runCmd("confluent kafka cluster list");
            String[] lines = currentCluster.split("\\r?\\n");
            for (int i = 2; i < lines.length; i++) {
                List<String> myList = Splitter.on('|').trimResults().splitToList(lines[i]);
                kcMap.put(myList.get(0), myList.get(1));
            }
            KAFKA_CLUSTER_LIST = kcMap;
        }
        return KAFKA_CLUSTER_LIST;
    }

    public static String getCurrentKafkaCluster() {
        for (Object s : KAFKA_CLUSTER_LIST.keySet()) {
            if (s.toString().startsWith("*")) {
                SELECTED_KAFKA_CLUSTER = "%s: %s".formatted(s.toString().substring(2), KAFKA_CLUSTER_LIST.get(s));
            }
        }
        return SELECTED_KAFKA_CLUSTER;
    }*/

    public static LogDataProvider getInstance() {
        return LazyHolder.INSTANCE;
    }

/*
    public static String getConfluentCliVersion() {
        return getInstance().CONFLUENT_VERSION;
    }

    public static String getKCatVersion() {
        if (KCAT_VERSION == null) {
            String kcatV = Utils.runCmd("kcat -V");
            //LOG.info(kcatV);
            String[] lines = kcatV.split("\\r?\\n");
            // LOG.info("Last:" + lines[lines.length - 1]);
            KCAT_VERSION = lines[lines.length - 1];
        }
        return KCAT_VERSION;
    } */

    public static Map getConfigs() {
        return getInstance().CONFIGS;
    }
    public static void setConfigs(Map m) {
        getInstance().CONFIGS = m;
    }

    /*
    public static String getCurrentEnvironment() {
        // FIXME - get the first env maybe?
        for (Object s : getEnvironments().keySet()) {
            if (s.toString().startsWith("*")) {
                SELECTED_ENVIRONMENT = "%s: %s".formatted(s.toString().substring(2), getEnvironments().get(s));
            }
        }
        LOG.info("* Selected environment: " + SELECTED_ENVIRONMENT);
        return SELECTED_ENVIRONMENT;
    }

    public static String getKafkaClusters(boolean modified) {
        // FIXME - what if an environment isn't in use?
        if (KAFKA_CLUSTER_JSON_LIST == null || modified) {
            LOG.info("retrieving Kafka cluster list...");
            KAFKA_CLUSTER_JSON_LIST = Utils.runCmd("confluent kafka cluster list --output json");
        }
        return KAFKA_CLUSTER_JSON_LIST;
    }

    public void setEnvironment(String env) {
        Utils.runCmd("confluent environment use %s".formatted(env));
        ENVIRONMENTS = Utils.getEnvs();
    }

    public void setKafkaCluster(String id) {
    } */

    private static class LazyHolder {
        private static final LogDataProvider INSTANCE = new LogDataProvider();
    }


}
