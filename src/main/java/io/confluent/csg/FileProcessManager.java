package io.confluent.csg;

import io.confluent.csg.providers.LogDataProvider;
import io.confluent.csg.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileProcessManager {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void processLogFile(String filename){
        try {
            InputStream is = new FileInputStream(filename);
            processLogFile(is, filename);
        } catch (FileNotFoundException e) {
            Utils.returnExceptionString(e);
        } catch (IOException e) {
            Utils.returnExceptionString(e);
        }
    }

    public void processLogFile(InputStream is, String filename) throws IOException {

        BufferedReader reader;
        try {
            Map logItemMap = new HashMap<String, List>();
            Map configMap = new HashMap<String, List>();

            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            String[] arr = LogDataProvider.getInstance().getConfig().getStringArray("topiclist");

            // All the things that we currently classify
            List rebalance = new ArrayList();
            List distributedHerder = new ArrayList();
            List license = new ArrayList();
            List kafkaInfo = new ArrayList();
            List workerState = new ArrayList<>();
            List groupCoordinator = new ArrayList();
            List metadata = new ArrayList();
            List tls = new ArrayList();
            List consumer = new ArrayList();
            List producer = new ArrayList();
            List workerTask = new ArrayList();
            List assigns = new ArrayList();
            List warns = new ArrayList();
            List unclassified = new ArrayList();
            List errors = new ArrayList();
            List unknownTopicOrPartition = new ArrayList();
            List schemaRegistry = new ArrayList();
            List kerberosTGT = new ArrayList();
            List exceptionList = new ArrayList();

            while (line != null) {
                if(line.contains("Rebalance")) {
                    rebalance.add(line);
                }
                // Fixing an issue where a line like this is logged:
                // Connector <name> config updated
                 else if(line.contains("config updated")) {
                    LOG.debug("should see this!");
                    distributedHerder.add(line);
                } else if (line.contains("License for control-center expires in")) {
                    license.add(line);
                } else if (line.contains("INFO Creating task ")) {
                    // TODO - extract full stack!
                } else if (line.contains("INFO Kafka")) {
                    // TODO - grab the Kafka version at the same time
                    kafkaInfo.add(line);
                } else if (line.contains("Setting connector")) {
                    workerState.add(line);
                } else if (line.contains("org.apache.kafka.connect.runtime.TransformationChain")) {
                    // TODO - extract all lines between this and (org.apache.kafka.clients.producer.ProducerConfig)
                } else if (line.contains("Group coordinator") || line.contains("Discovered group coordinator")
                || line.contains("internals.AbstractCoordinator") || line.contains("distributed.DistributedHerder")
                || line.contains("distributed.WorkerCoordinator")
                ) {
                    groupCoordinator.add(line);
                } else if (line.contains("Metadata update failed") || line.contains("fetchMetadata")) {
                    metadata.add(line);
                } else if (line.contains("INFO x509=")) {
                    tls.add(line);
                } else if (line.contains("Received unknown topic or partition error in fetch for partition")) {
                    unknownTopicOrPartition.add(line);
                } else if (line.contains("ERROR")) {
                    errors.add(line);
                } else if (line.contains("WARN")) {
                    if (!line.contains("was supplied but isn't a known config") && !line.contains("Error registering AppInfo mbean")) {
                        // Flag Other WARNs
                        warns.add(line);
                    }
                } else if (line.contains("INFO [Consumer")) {
                    consumer.add(line);
                } else if (line.contains("INFO [Producer")) {
                    producer.add(line);
                } else if (line.contains("INFO WorkerSourceTask")) {
                    if(!line.contains("flushing 0 outstanding messages for offset commit") && !line.contains("Committing offsets (org.apache.kafka.connect.runtime.WorkerSourceTask)")) {
                        workerTask.add(line);
                    }
                } else if (line.contains("computing task topic partition assignments")) {
                    assigns.add(line);
                } else if (line.contains("Found matching topics")) {
                    LOG.debug("matching topics logged in an array..");
                    Utils.processArrayFromLine(line, arr);
                } else if (line.contains("schema registry")) {
                    schemaRegistry.add(line);
                } else if (line.contains("TGT ") || line.contains("Initiating logout for") || line.contains("Initiating re-login for")) {
                    kerberosTGT.add(line);
                } else if (line.contains("values:")) {
                    // Is this configuration
                    Utils.processConfigurationBlock(line, reader, configMap);
                } else if ( (line.contains("org.apache.kafka.common.errors") || line.contains("java.util.concurrent"))
                        && line.contains("Exception")) {
                    // Generic Apache common Exception stack trace
                    Utils.processStackTrace(line, reader, exceptionList);
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
            logItemMap.put("rebalance", rebalance);
            logItemMap.put("distributedHerder", distributedHerder);
            logItemMap.put("license", license);
            logItemMap.put("kafkaInfo", kafkaInfo);
            logItemMap.put("workerState", workerState);
            logItemMap.put("groupCoordinator", groupCoordinator);
            logItemMap.put("metadata", metadata);
            logItemMap.put("tls", tls);
            logItemMap.put("consumer", consumer);
            logItemMap.put("producer", producer);
            logItemMap.put("workerTask", workerTask);
            logItemMap.put("assigns", assigns);
            logItemMap.put("warns", warns);
            logItemMap.put("errors", errors);
            logItemMap.put("unclassified", unclassified);
            logItemMap.put("unknownTopicOrPartition", unknownTopicOrPartition);
            logItemMap.put("schemaRegistry", schemaRegistry);
            logItemMap.put("kerberosTGT", kerberosTGT);
            logItemMap.put("exceptionList", exceptionList);

            LOG.info("File Parsing has completed.");
            Map<String, Integer> logSizeMap = new HashMap<>();
            for (Object s : logItemMap.keySet()) {
                List l = (List) logItemMap.get(s);
                logSizeMap.put(s.toString(),l.size());
                LOG. info("Total "+ s + "messages " + l.size());
            }

            // Finally, we will add everything to the LogDataProvider, so it's available in Jersey/Freemarker..
            LogDataProvider.getInstance().setConfigs(configMap);
            LogDataProvider.getInstance().setLogs(logItemMap);
            LogDataProvider.getInstance().setLogSizeMap(logSizeMap);
            LogDataProvider.getInstance().setFilename(filename);

            for (Object s : configMap.keySet()) {
                LOG.debug("Config for: " + s + configMap.get(s).toString());
            }

            reader.close();
        } catch (IOException e) {
            LOG.error("Exception Caught: " + e.getMessage(), e);
        }
        /*
        LOG.info(MessageFormat.format("Processing Uploaded ErrorLog file: {0}", filename));
        ErrorLog el = new ErrorLog();
        el.setName(filename);
        el.setErrorLogTxt(IOUtils.readLines(new InputStreamReader(is, Charset.forName("UTF-8"))));
        processErrorLog(el);
        LOG.info(MessageFormat.format("Completed processing ErrorLog file: {0}", filename)); */

    }
}
