package io.confluent.csg.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Consts {

    public static final String RESOURCE_PACKAGES = "io.confluent.csg.resources"; /*,io.swagger.jaxrs.listing"; */
    public static final String SYSTEM_INFORMATION = "OS: %s %s (%s) - %s (%s)".formatted(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"), System.getProperty("user.country"), System.getProperty("user.timezone"));
    public static final String JAVA_INFORMATION = "JDK: " + System.getProperty("java.version") + " - " + System.getProperty("java.version.date");
    public static final String BASE_DIRECTORY_ROOT = new StringBuilder().append(System.getProperty("user.dir")).append(File.separator).append(StringUtils.join(new String[]{"src", "main", "resources"}, File.separator)).toString();
    public static final String STATIC_RESOURCE_DIRECTORY_ROOT = new StringBuilder().append(BASE_DIRECTORY_ROOT).append(File.separator).append("assets").toString();
    public static final String URI_BASE = "http://0.0.0.0/";
    public static final int GRIZZLY_HTTP_PORT = 9992;

    public static final String ASSETS_DIRECTORY_PATH = "/assets";

    public final static Charset UTF_8_CHARSET = StandardCharsets.UTF_8;
    public final static String FREEMARKER_TEMPLATE_PATH = "freemarker";

}
