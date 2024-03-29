package io.confluent.csg.util;

import io.confluent.csg.FileProcessManager;
import io.confluent.csg.providers.JerseyServer;
import io.confluent.csg.providers.LogDataProvider;
import io.confluent.csg.resources.Log;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    /*,io.swagger.jaxrs.listing"; */
    public static String returnExceptionString(final Exception e) {
        return MessageFormat.format("{0} caught: {1}", e.getClass().getName(),
                e);
    }

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String lineAfterLogLevel(String line, String level) {
        return StringUtils.substringAfter(line, level);
    }

    public static void startApplication() {
        PropertiesConfiguration config = Utils.loadConfigurationFile();
        FileProcessManager fpm = new FileProcessManager();
        fpm.processLogFile(config.getString("logfile"));

        // TODO - if nothing is parsed, Jersey starts anyway (and probably shouldn't!)
        // Now start Jersey:
        final Thread t = new JerseyServer();
        LOG.info("Starting JerseyServer");
        t.start();
    }

    public static PropertiesConfiguration loadConfigurationFile() {
        PropertiesConfiguration config = null;
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(
                            PropertiesConfiguration.class)
                            .configure(params.fileBased()
                                    .setFileName("config.properties")
                                    .setListDelimiterHandler(
                                            new DefaultListDelimiterHandler(','))
                                    .setThrowExceptionOnMissing(true));
            config = builder.getConfiguration();
        } catch (ConfigurationException cex) {
            LOG.error(MessageFormat.format("Properties Configuration Exception Caught: {0}", cex.getMessage()), cex);
        }
        LogDataProvider.getInstance().setConfig(config);
        return LogDataProvider.getInstance().getConfig();
    }

    /**
     * To get this list run: grep "values:" /var/log/kafka/connect-distributed.log | cut -d " " -f4 | sort | uniq
     * AbstractConfig
     * AdminClientConfig
     * ConnectorConfig
     * ConsumerConfig
     * ConsumerOffsetsTranslatorConfig
     * DistributedConfig
     * EnrichedConnectorConfig
     * JsonConverterConfig
     * MonitoringInterceptorConfig
     * ProducerConfig
     * ReplicatorSourceConnectorConfig
     * ReplicatorSourceTaskConfig
     * SourceConnectorConfig
     * TaskConfig
     * WorkerInfo
     */
    public static void processConfigurationBlock(String startingLine, BufferedReader reader, Map configMap) throws IOException {
        LOG.debug("in ProcessConfigurationBlock");
        // Get the specific value (as item)
        String l2 = Utils.lineAfterLogLevel(startingLine, "INFO");
        String item = StringUtils.substringBefore(l2.trim(), "values:").trim();
        String endMarker = item + ")";
        List configArray = new ArrayList();
        // Skip to next line immediately
        String subsequentLine = reader.readLine();
        if (subsequentLine.startsWith("[") && subsequentLine.contains("]")) {
            LOG.info("no stacktrace - TODO - dumping in array for now");
            configArray.add(subsequentLine);
        } else if (!configMap.containsKey(item)) {
            LOG.debug("Start Marker: " + item);
            LOG.debug("End Marker should be:" + endMarker);
            LOG.debug("First Line of Config: " + subsequentLine);

            // Add to the Map if necessary

            while (!StringUtils.contains(subsequentLine, endMarker)) {
                configArray.add(subsequentLine);
                subsequentLine = reader.readLine();
            }
            configMap.put(item, configArray);
            LOG.debug("Last Line of Config: " + subsequentLine);
        }

    }

    public static void processStackTrace(String startingLine, BufferedReader reader, List exceptionList) throws IOException {
        // TODO - add to some kind of Exception map to track the frequency
        exceptionList.add(startingLine);
        String subsequentLine = reader.readLine();
        if (subsequentLine.startsWith("[") && subsequentLine.contains("]")) {
            LOG.info("no stacktrace - skipping");
        } else {
            String endMarker = "at java.lang.Thread.run";
            while (!StringUtils.contains(subsequentLine, endMarker) && !subsequentLine.contains("INFO") && !subsequentLine.contains("WARN") && !subsequentLine.contains("ERROR")) {
                exceptionList.add(subsequentLine);
                subsequentLine = reader.readLine();
            }
        }
    }

    public static void processArrayFromLine(String line, String[] arr) {
        // TODO - fine for now - but should handle other log levels
        String line2 = lineAfterLogLevel(line, "INFO");
        if (line2 != null && line2.contains("[") && line2.contains("]")) {
            String items = StringUtils.substringBetween(line2, "[", "]");
            if (items.contains(",")) {
                //LOG.info(line);
                LOG.debug("Items: " + items.split(",").length);
                //LOG.info("?"+StringUtils.containsAny(items, arr));

                for (String s : arr) {
                    //LOG.info("?"+StringUtils.contains(items, s));
                    if (!StringUtils.contains(items, s)) {
                        LOG.debug("nope" + s);
                    }
                }
                //LOG.info(items);

            }

        }
    }

}
