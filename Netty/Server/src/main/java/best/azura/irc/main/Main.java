package best.azura.irc.main;

import best.azura.irc.server.Server;
import best.azura.irc.sql.SQLConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Main instance;
    private SQLConnector sqlConnector;
    private Server server;
    private Logger logger;
    private Logger analyticLogger;

    public static void main(String[] args) throws Exception {
        instance = new Main();
        instance.sqlConnector = new SQLConnector("irc", "irc", "password", "localhost", 3306);
        instance.logger = LoggerFactory.getLogger(Main.class);
        instance.analyticLogger = LoggerFactory.getLogger("analytic");
        instance.server = new Server(6969);
        instance.server.start();
    }

    public static Main getInstance() {
        return instance;
    }

    public Server getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Logger getAnalyticsLogger() {
        return analyticLogger;
    }

    public SQLConnector getSQLConnector() {
        return sqlConnector;
    }
}
