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
                    LOG.info("assignment detected");
                }

                //LOG.info(StringUtils.substringAfter(line, "INFO"));
                String line2 = StringUtils.substringAfter(line, "INFO");

                if(line2.contains("[") && line2.contains("]")){
                    String items = StringUtils.substringBetween(line2, "[","]");
                    if(items.contains(",")){
                        //LOG.info(line);
                        //LOG.info("Items: "+ items.split(",").length);
                        //LOG.info("?"+StringUtils.containsAny(items, arr));

                        for (String s : arr){
                            //LOG.info("?"+StringUtils.contains(items, s));
                            if (!StringUtils.contains(items, s)) {
                                //LOG.info("nope" + s);
                            }
                        }
                        //LOG.info(items);

                    }

                }
                //LOG.info(line);
                // read next line

                line = reader.readLine();
            }

            LOG.info ("Total WARN level messages: "+warns);
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
