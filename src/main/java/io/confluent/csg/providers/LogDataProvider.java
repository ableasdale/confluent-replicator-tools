package io.confluent.csg.providers;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class LogDataProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Map CONFIGS;
    private static Map LOGS;
    private static Map LOG_SIZES;
    private static PropertiesConfiguration CONFIG = null;
    private static String FILENAME;


    public void setFilename(String s){
        FILENAME = s;
    }

    public String getFilename(){
        return FILENAME;
    }

    public void setConfig(PropertiesConfiguration pc){
        CONFIG = pc;
    }

    public PropertiesConfiguration getConfig(){
        return CONFIG;
    }

    private LogDataProvider() {
        LOG.debug("LogDataProvider :: instantiating");
    }

    public static LogDataProvider getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static Map getConfigs() {
        return getInstance().CONFIGS;
    }

    public static void setConfigs(Map m) {
        getInstance().CONFIGS = m;
    }

    public static Map getLogs() {
        return getInstance().LOGS;
    }

    public static void setLogs(Map m) {
        getInstance().LOGS = m;
    }

    public static Map getLogSizeMap() {return getInstance().LOG_SIZES;}

    public static void setLogSizeMap(Map m) {
        getInstance().LOG_SIZES = m;
    }

    private static class LazyHolder {
        private static final LogDataProvider INSTANCE = new LogDataProvider();
    }


}
