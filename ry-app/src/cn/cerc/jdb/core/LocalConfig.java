//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.cerc.jdb.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalConfig implements IConfig {
    private static final Logger log = LoggerFactory.getLogger(LocalConfig.class);
    private static Properties properties = new Properties();
    private static LocalConfig instance;

    public LocalConfig() {
        if (instance != null) {
            log.error("LocalConfig instance is not null");
        }

        instance = this;
        String confFile = System.getProperty("user.home") + System.getProperty("file.separator") + "summer-application.properties";

        try {
            File file2 = new File(confFile);
            if (file2.exists()) {
                properties.load(new FileInputStream(confFile));
                log.info("read properties from : " + confFile);
            } else {
//                log.error("not find properties: " + confFile);
            }
        } catch (FileNotFoundException var3) {
            log.error("The settings file '" + confFile + "' does not exist.");
        } catch (IOException var4) {
            log.error("Failed to load the settings from the file: " + confFile);
        }

    }

    public String getProperty(String key, String def) {
        String result = null;
        if (properties != null) {
            result = properties.getProperty(key);
        }

        return result != null ? result : def;
    }

    public String getProperty(String key) {
        return this.getProperty(key, (String)null);
    }

    public static LocalConfig getInstance() {
        if (instance == null) {
            new LocalConfig();
        }

        return instance;
    }

    public static void main(String[] args) {
        LocalConfig config = new LocalConfig();
        System.out.println(config.getProperty("key"));
    }
}
