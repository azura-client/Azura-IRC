package best.azura.irc.utils;

import lombok.extern.slf4j.Slf4j;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Slf4j
public class Config {

    private YamlFile yamlConfiguration;

    public Config() {
        load();
    }

    public void load() {
        try {
            Path storage = Path.of("config");
            Path cert = Path.of("config/irc.keystore");

            if (!Files.exists(storage))
                Files.createDirectory(storage);

            if (!Files.exists(cert))
                Files.writeString(cert, "");

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
            yamlConfiguration.addDefault("keystore.password", "password");
            yamlConfiguration.addDefault("keystore.location", "config/irc.keystore");

            try {
                yamlConfiguration.save(getFile());
            } catch (IOException e) {
                log.error("Could not save config.yml!", e);
            }
        }
    }

    public String getKeystore() {
        try (FileInputStream fileInputStream = new FileInputStream(yamlConfiguration.getString("keystore.location", "config/irc.keystore"))) {
            byte[] bytes = fileInputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            log.error("Could not read keystore!", e);
        }

        return null;
    }

    public String getString(String path) {
        return getConfiguration().getString(path);
    }

    public int getInt(String path) {
        return getConfiguration().getInt(path);
    }

    public YamlFile createConfiguration() {
        try {
            return YamlFile.loadConfiguration(getFile());
        } catch (Exception ignore) {
            log.error("Failed to load config.yml", ignore);
            return new YamlFile();
        }
    }

    public YamlFile getConfiguration() {
        if (yamlConfiguration == null) {
            return yamlConfiguration = createConfiguration();
        }

        return yamlConfiguration;
    }

    public File getFile() {
        return new File("config/config.yml");
    }

}
