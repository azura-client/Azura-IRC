package best.azura.irc.main;

import best.azura.irc.server.Server;
import best.azura.irc.utils.Config;
import best.azura.irc.utils.VersionUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import io.sentry.Sentry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Security;
import java.util.Set;

import static java.lang.System.exit;

@Slf4j
@Getter
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

        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.getDefaultFactory());

        instance.server = new Server(config.getInt("server.port"), config.getKeystore(), config.getString("keystore.password"));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> instance.server.terminate()));

        instance.server.start();

        waitUntilKeypressed();
        exit(0);
    }

    private static void waitUntilKeypressed() {
        try {
            System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (IOException e) {
            log.error("Failed to wait until keypress!", e);
        }
    }
}
