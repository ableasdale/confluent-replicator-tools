package io.confluent.csg;

import io.confluent.csg.providers.JerseyServer;
import io.confluent.csg.providers.LogDataProvider;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;

public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws IOException {

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
        LogDataProvider.getInstance().setConfig(config);

        FileProcessManager fpm = new FileProcessManager();
        InputStream is = new FileInputStream(config.getString("logfile"));

        fpm.processLogFile(is, config.getString("logfile"));

        // TODO - if nothing is parsed, Jersey starts anyway (and probably shouldn't!)
        // Now start Jersey:
        final Thread t = new JerseyServer();
        LOG.info("Starting JerseyServer");
        t.start();
    }
}
