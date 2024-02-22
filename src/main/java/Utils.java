import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static String lineAfterLogLevel(String line, String level) {
        return StringUtils.substringAfter(line, level);
    }

    protected static void processConfigurationBlock(String startingLine, BufferedReader reader, Map configMap) throws IOException {
        // Get the specific value (as item)
        String l2 = Utils.lineAfterLogLevel(startingLine, "INFO");
        String item = StringUtils.substringBefore(l2.trim(), "values:").trim();
        String endMarker = item+")";
        // Skip to next line immediately
        String subsequentLine = reader.readLine();

        // Add to the Map if necessary
        if (!configMap.containsKey(item)) {
            LOG.info("Start Marker: " + item);
            LOG.debug("End Marker should be:" + endMarker);
            LOG.debug("First Line of Config: "+subsequentLine);

            List configArray = new ArrayList();
            while (!StringUtils.contains(subsequentLine, endMarker)){
                configArray.add(subsequentLine);
                subsequentLine = reader.readLine();
            }
            configMap.put(item, configArray);
            LOG.info("Last Line of Config: "+subsequentLine);
        }

        /*
        int counter = 0;


        // Add to the Map if necessary
        if (!configMap.containsKey(item)) {
            LOG.info("Start Marker: " + item);
            LOG.info("start at" + line);
            //List configArray = new ArrayList();
            //LOG.debug("Adding Configuration for: " + item);
                        /*
                        LOG.info(line);
                        while (!line.contains(item) || counter < 150){
                            //line != null &&
                            ++counter;
                            LOG.info("adding: "+line);
                            configArray.add(line);
                            line = reader.readLine();
                        }


            while (!StringUtils.contains(line, item)) {
                line = reader.readLine();
            }

        }
                        while (!StringUtils.endsWith(line, item+")")) {
                            line = reader.readLine();
                        }
        LOG.info("done at"+line);
    } */
    }
    protected static void processArrayFromLine(String line, String[] arr){
        // TODO - fine for now - but should handle other log levels
        String line2 = lineAfterLogLevel(line, "INFO");
        if(line2 != null && line2.contains("[") && line2.contains("]")){
            String items = StringUtils.substringBetween(line2, "[","]");
            if(items.contains(",")){
                //LOG.info(line);
                LOG.debug("Items: "+ items.split(",").length);
                //LOG.info("?"+StringUtils.containsAny(items, arr));

                for (String s : arr){
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
