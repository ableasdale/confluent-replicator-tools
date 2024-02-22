import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

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
            //LOG.info("L"+config.getString("logfile"));

            // access configuration properties
            //if(config.getString("logfile") ;
        } catch (ConfigurationException cex) {
            cex.printStackTrace();
        }

        BufferedReader reader;

        try {
            //int lineno = 1;
            int warns = 0;
            int assigns = 0;
            boolean isProducerConfigStored = false;
            boolean isConsumerConfigStored = false;
            boolean isAdminClientConfigStored = false;
            boolean isSourceConnectorConfigStored = false;

            List producerConfig = new ArrayList<String>();
            List consumerConfig = new ArrayList<String>();
            List adminClientConfig = new ArrayList<String>();
            List sourceConnectorConfig = new ArrayList<String>();
            reader = new BufferedReader(new FileReader(config.getString("logfile")));
            String line = reader.readLine();
            String[] arr = config.getStringArray("topiclist");

            while (line != null) {
                // Flag WARNs
                if (line.contains("WARN")) {
                    if (!line.contains("was supplied but isn't a known config") && !line.contains("Error registering AppInfo mbean")) {
                        ++warns;
                        LOG.warn(line);
                    }
                }

                if (line.contains("computing task topic partition assignments")) {
                    ++assigns;
                    LOG.info("assignment detected");
                }

                if (line.contains("Found matching topics")) {
                    LOG.debug("matching topics logged in an array..");
                    Utils.processArrayFromLine(line, arr);
                }

                /**
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

                
                if (line.contains("ProducerConfig values:")) {
                    if (!isProducerConfigStored) {
                        LOG.info("Found Replicator Producer config" + line);
                        line = reader.readLine();
                        while (!line.contains("(org.apache.kafka.clients.producer.ProducerConfig)")) {
                            producerConfig.add(line);
                            line = reader.readLine();
                        }
                    }
                    isProducerConfigStored = true;
                }
                if (line.contains("ConsumerConfig values:")) {
                    if (!isConsumerConfigStored) {
                        LOG.info("Found Replicator Consumer config" + line);
                        line = reader.readLine();
                        while (!line.contains("(org.apache.kafka.clients.consumer.ConsumerConfig)")) {
                            consumerConfig.add(line);
                            line = reader.readLine();
                        }
                    }
                    isConsumerConfigStored = true;
                }


                if (line.contains("AdminClientConfig values:")) {
                    if (!isAdminClientConfigStored) {
                        LOG.info("Found Replicator Admin Client config" + line);
                        line = reader.readLine();
                        while (!line.contains("(org.apache.kafka.clients.admin.AdminClientConfig)")) {
                            adminClientConfig.add(line);
                            line = reader.readLine();
                        }
                    }
                    isAdminClientConfigStored = true;
                }

                if (line.contains("SourceConnectorConfig values:")) {
                    if (!isSourceConnectorConfigStored) {
                        LOG.info("Found Replicator Source Connector config" + line);
                        line = reader.readLine();
                        while (!line.contains("(org.apache.kafka.connect.runtime.SourceConnectorConfig)")) {
                            sourceConnectorConfig.add(line);
                            line = reader.readLine();
                        }
                    }
                    isSourceConnectorConfigStored = true;
                }


                // Is Replicator logging Array information?
                Utils.processArrayFromLine(line, arr);

                //LOG.info(line);
                // read next line

                line = reader.readLine();
            }

            LOG.info("Total WARN level messages: " + warns);
            LOG.info("Total Assignment messages: " + assigns);
            LOG.info("Producer: " + producerConfig.toString());
            LOG.info("Consumer: " + consumerConfig.toString());
            LOG.info("Admin Client: " + adminClientConfig.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        LOG.info(arr1);
        LOG.info("L"+ arr1.split(",").length);
        LOG.info("L"+ arr1.split(",")[1]);
        for (String s : arr1.split(",")){
            //LOG.info("n:"+s);
        } */
    }
}
