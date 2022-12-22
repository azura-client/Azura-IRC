package best.azura.irc.main;

import best.azura.irc.server.Server;
import best.azura.irc.utils.Config;
import best.azura.irc.utils.VersionUtil;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Security;
import java.util.Set;

@Slf4j
public class Main {

    private static Main instance;
    private Server server;

    private static Config config;

    public static void main(String[] args) throws Exception {
        instance = new Main();
        config = new Config();

        /*Sentry.init(options -> {
            options.setDsn(config.getString("sentry.dsn") != null ? config.getString("sentry.dsn") : "");
            options.setRelease(VersionUtil.getVersion());
        });*/

        instance.server = new Server(6969, config.getKeystore(), config.getString("keystore.password"));
        instance.server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> instance.server.terminate()));
    }

    public static Main getInstance() {
        return instance;
    }

    public Server getServer() {
        return server;
    }
}
