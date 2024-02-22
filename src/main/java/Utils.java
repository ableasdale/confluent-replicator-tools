import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static String lineAfterLogLevel(String line, String level) {
        return StringUtils.substringAfter(line, level);
    }

    protected static void processArrayFromLine(String line, String[] arr){
        // TODO - fine for now - but should handle other log levels
        String line2 = lineAfterLogLevel(line, "INFO");
        if(line2.contains("[") && line2.contains("]")){
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
