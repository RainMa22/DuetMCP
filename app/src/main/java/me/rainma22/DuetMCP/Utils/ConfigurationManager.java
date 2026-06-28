package me.rainma22.DuetMCP.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 */
public class ConfigurationManager {

    private static final Path CONF_DIR_PATH = Path.of(".",
            "confs");

    private Path confPath;

    private ConfigurationManager(Class<?> c) {
        confPath = CONF_DIR_PATH.resolve(c.getSimpleName().concat(".json"));
    }

    public JSONObject getConfig(Map<String, ? extends Object> defaultConfigMap) {
        return getConfig(new JSONObject(defaultConfigMap));
    }

    public JSONObject getConfig(JSONObject defaultConfig) {
        if (!confPath.toFile().exists()) {
            commitConfig(defaultConfig);
        }
        try (var in = new FileInputStream(confPath.toFile())) {
            return new JSONObject(new String(in.readAllBytes()));
        } catch (IOException ex) {
            System.getLogger(ConfigurationManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return defaultConfig;
        }
    }

    public void commitConfig(JSONObject jo) {
        try (var out = new FileOutputStream(confPath.toFile())) {
            out.write(jo.toString(4).getBytes("UTF-8"));
        } catch (FileNotFoundException ex) {
            System.getLogger(ConfigurationManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(ConfigurationManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public static ConfigurationManager ofClass(Class<?> c) {
        CONF_DIR_PATH.toFile().mkdirs();
        return new ConfigurationManager(c);
    }

}
