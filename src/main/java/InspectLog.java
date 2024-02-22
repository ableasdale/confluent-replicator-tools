import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class InspectLog {
    
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

        Configurations configs = new Configurations();
        Configuration config = null;
        try
        {
            config = configs.properties(new File("config.properties"));
            // access configuration properties
            //if(config.getString("logfile") ;
        }
        catch (ConfigurationException cex)
        {
            cex.printStackTrace();
        }

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(config.getString("logfile")));
            String line = reader.readLine();

            while (line != null) {
                // does the line contain an array?

                //LOG.info(StringUtils.substringAfter(line, "INFO"));
                String line2 = StringUtils.substringAfter(line, "INFO");

                if(line2.contains("[") && line2.contains("]")){
                    String items = StringUtils.substringBetween(line2, "[","]");
                    if(items.contains(",")){
                        //LOG.info(line);
                        LOG.info("Items: "+ items.split(",").length);
                        //LOG.info("?"+StringUtils.containsAny(items, arr));
                        String[] arr = config.getStringArray("topiclist");
                        for (String s : arr){
                            //LOG.info("?"+StringUtils.contains(items, s));
                            if (!StringUtils.contains(items, s)) {
                                LOG.info("nope" + s);
                            }
                        }
                        //LOG.info(items);

                    }

                }
                //LOG.info(line);
                // read next line

                line = reader.readLine();
            }

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
