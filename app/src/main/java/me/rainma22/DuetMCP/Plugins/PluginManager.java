package me.rainma22.DuetMCP.Plugins;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;

/**
 *
 */
public class PluginManager {

    private ResourceRegistries registries;
    private final System.Logger LOGGER = System.getLogger(PluginManager.class.getName());

    public PluginManager(ResourceRegistries registries) {
        this.registries = registries;
    }

    public void loadPlugins(Path pluginPath) {
        if (!pluginPath.toFile().exists()) {
            pluginPath.toFile().mkdir();
        }

        for (File f : pluginPath.toFile().listFiles()) {
            LOGGER.log(System.Logger.Level.INFO, "Attempting to load file: "
                    + f.getName());
            try {
                JarFile jar = new JarFile(f);
                Manifest man = jar.getManifest();
                if (man == null) {
                    throw new IOException(String.format(
                            "Bad plugin File %s", jar.getName()));
                }
                String mainClassStr = man.getMainAttributes().getValue("Main-Class");
                if (mainClassStr == null || mainClassStr.trim().isEmpty()) {
                    throw new IOException(String.format(
                            "No Manifest found for JarFile %s", jar.getName()));
                }
                URLClassLoader loader = new URLClassLoader(
                        new URL[]{f.toURI().toURL()},
                        this.getClass().getClassLoader());
                Class<Plugin> c = (Class<Plugin>) loader.loadClass(mainClassStr);
                
                c.getDeclaredConstructor().newInstance().onLoad(registries);

                LOGGER.log(System.Logger.Level.INFO, "loaded file: "
                        + f.getName());
            } catch (Exception | Error e) {
                Arrays.asList(e.getStackTrace()).forEach(st
                        -> LOGGER.log(System.Logger.Level.WARNING, st)
                );
                LOGGER.log(System.Logger.Level.WARNING,
                        String.format("Skipping loading file: %s", f.getName()));
            }
        }
    }
}
