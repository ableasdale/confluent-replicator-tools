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
import java.util.List;

public class InspectLog {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

        PropertiesConfiguration config = null;
        try
        {

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
        }
        catch (ConfigurationException cex)
        {
            cex.printStackTrace();
        }

        BufferedReader reader;

        try {
            int warns = 0;
            int assigns = 0;
            reader = new BufferedReader(new FileReader(config.getString("logfile")));
            String line = reader.readLine();
            String[] arr = config.getStringArray("topiclist");

            while (line != null) {
                // Flag WARNs
                if(line.contains("WARN")) {
                    if (!line.contains("was supplied but isn't a known config") && !line.contains("Error registering AppInfo mbean")) {
                        ++warns;
                        LOG.warn(line);
                    }
                }

                if(line.contains("computing task topic partition assignments")) {
                    ++assigns;
                    LOG.info("assignment detected");
                }

                if(line.contains("Found matching topics")){
                    LOG.debug("matching topics logged in an array..");
                    Utils.processArrayFromLine(line,arr);
                }

                // Is Replicator logging Array information?
                Utils.processArrayFromLine(line,arr);

                //LOG.info(line);
                // read next line

                line = reader.readLine();
            }

            LOG.info ("Total WARN level messages: "+warns);
            LOG.info ("Total Assignment messages: "+assigns);
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
