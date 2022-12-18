package best.azura.irc.utils;

import lombok.extern.slf4j.Slf4j;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Config {

    private static YamlFile yamlConfiguration;

    public static void load() {
        try {
            Path storage = Path.of("config");
            if (!Files.exists(storage))
                Files.createDirectory(storage);
        } catch (Exception exception) {
            log.error("Could not create Config folder!", exception);
        }

        getConfiguration();

        if (!getFile().exists()) {
            yamlConfiguration.options()
                    .copyDefaults(true)
                    .copyHeader(true)
                    .header("""
                            ##################################
                            # Azura IRC Server Configuration #
                            ##################################
                            """);

            yamlConfiguration.addDefault("server.port", 6969);
            yamlConfiguration.addDefault("sentry.dsn", "https://sentry.io/");
        }
    }

    public static String getString(String path) {
        return getConfiguration().getString(path);
    }

    public static YamlFile createConfiguration() {
        try {
            return new YamlFile(getFile());
        } catch (Exception ignore) {
            log.error("Failed to load config.yml", ignore);
            return new YamlFile();
        }
    }

    public static YamlFile getConfiguration() {
        if (yamlConfiguration == null) {
            yamlConfiguration = createConfiguration();
        }

        return yamlConfiguration;
    }

    public static File getFile() {
        return new File("config/config.yml");
    }

}
