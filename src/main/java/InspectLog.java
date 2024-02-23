import io.confluent.csg.providers.JerseyServer;
import io.confluent.csg.providers.LogDataProvider;
import io.confluent.csg.util.Utils;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspectLog {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

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
            LOG.error("Exception Caught: " + cex.getMessage(), cex);
        }

        BufferedReader reader;

        try {
            Map logItemMap = new HashMap<String, List>();
            Map configMap = new HashMap<String, List>();

            reader = new BufferedReader(new FileReader(config.getString("logfile")));
            String line = reader.readLine();
            String[] arr = config.getStringArray("topiclist");

            // All the things that we currently classify
            List consumer = new ArrayList<>();
            List producer = new ArrayList<>();
            List workerTask = new ArrayList<>();
            List assigns = new ArrayList();
            List warns = new ArrayList();
            List unclassified = new ArrayList();
            List errors = new ArrayList();
            List unknownTopicOrPartition = new ArrayList();

            while (line != null) {
                if (line.contains("Received unknown topic or partition error in fetch for partition")) {
                    unknownTopicOrPartition.add(line);
                } else if (line.contains("ERROR")) {
                    errors.add(line);
                } else if (line.contains("WARN")) {
                    // Flag Other WARNs
                    if (!line.contains("was supplied but isn't a known config") && !line.contains("Error registering AppInfo mbean")) {
                        warns.add(line);
                        //LOG.warn(line);
                    }
                } else if (line.contains("INFO [Consumer")) {
                    consumer.add(line);
                } else if (line.contains("INFO [Producer")) {
                    producer.add(line);
                } else if (line.contains("INFO WorkerSourceTask")) {
                    workerTask.add(line);
                } else if (line.contains("computing task topic partition assignments")) {
                    assigns.add(line);
                    // TODO
                    //LOG.info("assignment detected");
                } else if (line.contains("Found matching topics")) {
                    LOG.debug("matching topics logged in an array..");
                    Utils.processArrayFromLine(line, arr);
                } else if (line.contains("values:")) {
                    // Is this configuration
                    Utils.processConfigurationBlock(line, reader, configMap);
                } else {
                    // Throw it into "unclassified"
                    unclassified.add(line);
                }

                // Is Replicator logging Array information?
                Utils.processArrayFromLine(line, arr);

                // and move on to the next line
                line = reader.readLine();
            }
            // All done - collate results and add them to the LogDataProvider as a Map
            logItemMap.put("consumer", consumer);
            logItemMap.put("producer", producer);
            logItemMap.put("workerTask", workerTask);
            logItemMap.put("assigns", assigns);
            logItemMap.put("warns", warns);
            logItemMap.put("errors", errors);
            logItemMap.put("unclassified", unclassified);
            logItemMap.put("unknownTopicOrPartition", unknownTopicOrPartition);
            LogDataProvider.setLogs(logItemMap);

            LOG.info("File Parsing complete.");
            // iterate over keys
            for (Object s : logItemMap.keySet()) {
                List l = (List) logItemMap.get(s);
                LOG. info("Total "+ s + "messages " + l.size());
            }

            // Add the configMap..
            LogDataProvider.setConfigs(configMap);

            for (Object s : configMap.keySet()) {
                LOG.debug("Config for: " + s + configMap.get(s).toString());
            }

            reader.close();
        } catch (IOException e) {
            LOG.error("Exception Caught: " + e.getMessage(), e);
        }

        // TODO - if nothing is parsed, Jersey starts anyway (and probably shouldn't!)
        // Now start Jersey:
        final Thread t = new JerseyServer();
        LOG.info("Starting JerseyServer");
        t.start();
    }
}
