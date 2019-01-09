package com.huagu.vcoin.util;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author lu.li
 *
 */
public class Configuration {

    private static Object lock = new Object();
    private static Configuration config = null;
    private static ResourceBundle rb = null;
    private static final String CONFIG_FILE = "application";

    private Configuration() {
        rb = ResourceBundle.getBundle(CONFIG_FILE);
    }

    public static Configuration getInstance() {
        synchronized (lock) {
            if (null == config) {
                config = new Configuration();
            }
        }
        return (config);
    }

    public String getValue(String key) {
        return (rb.getString(key));
    }

    public static boolean isMasterServer() {
        try {
            return "master".equals(getInstance().getValue("server"));
        } catch (MissingResourceException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("ruizton");
        Enumeration<String> result = bundle.getKeys();
        while (result.hasMoreElements()) {
            String key = result.nextElement();
            System.out.println(key + ":" + bundle.getString(key));
        }
    }
}
