import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            List consumer = new ArrayList<>();
            List producer = new ArrayList<>();
            List workerTask = new ArrayList<>();
            List assigns = new ArrayList();
            List warns = new ArrayList();
            List unclassified = new ArrayList();

            Map configMap = new HashMap<String, List>();

            reader = new BufferedReader(new FileReader(config.getString("logfile")));
            String line = reader.readLine();
            String[] arr = config.getStringArray("topiclist");

            // TODO: Flag ERROR level messages?

            while (line != null) {
                // Flag WARNs
                if (line.contains("WARN")) {
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

            LOG.info("Total Consumer messages: " + consumer.size());
            LOG.info("Total Producer messages: " + producer.size());
            LOG.info("Total WorkerTask messages: " + workerTask.size());
            LOG.info("Total WARN level messages: " + warns.size());
            LOG.info("Total Assignment messages: " + assigns.size());
            LOG.info("Total Unclassified messages: " + unclassified.size());
            for (Object s : configMap.keySet()) {
                LOG.info("Config for: " + s + configMap.get(s).toString());
            }

            reader.close();
        } catch (IOException e) {
            LOG.error("Exception Caught: " + e.getMessage(), e);
        }
    }
}
