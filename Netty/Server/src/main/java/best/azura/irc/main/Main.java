package best.azura.irc.main;

import best.azura.irc.server.Server;
import best.azura.irc.utils.Config;
import best.azura.irc.utils.VersionUtil;
import io.sentry.Sentry;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static Main instance;
    private Server server;

    public static void main(String[] args) throws Exception {
        instance = new Main();
        Config.load();

        Sentry.init(options -> {
            options.setDsn(Config.getConfiguration().getString("sentry.dsn"));
            options.setRelease(VersionUtil.getVersion());
        });

        instance.server = new Server(6969, Files.readString(Path.of("config/cert.txt")), Config.getString("keystore.password"));
        instance.server.start();
    }

    public static Main getInstance() {
        return instance;
    }

    public Server getServer() {
        return server;
    }
}
