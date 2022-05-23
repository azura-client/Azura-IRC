package best.azura.irc.main;

import best.azura.irc.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    // Instance of the Main.class.
    private static Main instance;

    // Instance of the Server.
    private Server server;

    // Logger Instance.
    public Logger logger;

    /**
     * Constructor for Main class.
     */
    public Main() {
        // TODO used SecuredSSLSocket or Netty.
    }

    /**
     * Start methode for Application.
     * @param args Start Arguments.
     */
    public static void main(String[] args) {

        instance = new Main();

        instance.logger = LoggerFactory.getLogger(instance.getClass());

        instance.server = new Server(696969);

        instance.server.startServer();
    }

    /**
     * Retrieve the Main instance.
     * @return main instance.
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Change the Main instance.
     * @param instance new main instance.
     */
    @Deprecated(forRemoval = true)
    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    /**
     * Retrieve Server instance.
     * @return current server instance.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Change the Server instance.
     * @param server new server instance.
     */
    @Deprecated(forRemoval = true)
    public void setServer(Server server) {
        this.server = server;
    }


    /**
     * Retrieve the Logger.
     * @return the Logger.
     */
    public Logger getLogger() {
        return logger;
    }
}
