package best.azura.irc.server;

import best.azura.irc.channels.ChannelManager;
import best.azura.irc.command.CommandManager;
import best.azura.irc.connections.handler.ConnectionHandler;
import best.azura.irc.connections.handler.PacketHandler;
import best.azura.irc.connections.packets.PacketManager;
import best.azura.irc.main.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.ServerSocket;

public class Server {

    // Server-Port to bind the Server.
    private final int port;

    // Instance of the actual Server-Socket.
    private ServerSocket serverSocket;

    // Instance of the Connection Handler used to handle Incoming Connections.
    private ConnectionHandler connectionHandler;

    // Instance of the Packet Handler used to handle Incoming Packets.
    private PacketHandler packetHandler;

    // Instance of the Command Manager used to handle incoming commands.
    private CommandManager commandManager;

    // Instance of the Packet Manager to store all valid Packets.
    private PacketManager packetManager;

    // Instance of the UserManager used to save Users.
    private UserManager userManager;

    // Instance of the ChannelManager use to manage the Channels.
    private ChannelManager channelManager;

    // Instance of GSON.
    private Gson gson;

    /**
     * Constructor for the Server.
     * @param port bind port.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Call to start the Server and initialize the Server-Socket instance.
     */
    public void startServer() {
        try {
            gson = new GsonBuilder().create();

            // Create a new Instance of the ServerSocket.
            serverSocket = new ServerSocket(port);

            channelManager = new ChannelManager();

            connectionHandler = new ConnectionHandler(this);

            packetHandler = new PacketHandler(this);

            commandManager = new CommandManager(this);

            packetManager = new PacketManager();

            userManager = new UserManager();

            connectionHandler.startHandler();

            Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));

            Main.getInstance().getLogger().info("Running Server on port: " + port);

        } catch (Exception ex) {
            // Startup failed.
            Main.getInstance().getLogger().error("Couldn't start Server on port: " + port, ex);
        }
    }

    /**
     * Call to stop the Server.
     */
    public void stopServer() {
        try {
            serverSocket.close();
            connectionHandler.stopHandler();
        } catch (Exception ignore) {}
    }

    /**
     * Retrieve an Instance of the Server-Socket
     * @return Server-Socket Instance
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Replace the Server Instance.
     * @param serverSocket the new Server-Socket.
     */
    @Deprecated
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Retrieve an Instance of the Connection-Handler
     * @return Connection-Handler Instance.
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    /**
     * Replace the Connection-Handler
     * @param connectionHandler the new Connection-Handler
     */
    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    /**
     * Retrieve an Instance of the Packet-Handler
     * @return Packet-Handler Instance.
     */
    public PacketHandler getPacketHandler() {
        return packetHandler;
    }


    /**
     * Retrieve an Instance of the Channel-Manager.
     * @return Channel-Manager Instance.
     */
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * Retrieve an Instance of the Command-Manager.
     * @return Command-Manager Instance
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Replace the Packet-Handler
     * @param packetHandler the new Packet-Handler
     */
    public void setPacketHandler(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    /**
     * Replace the Command-Manager
     * @param commandManager the new Command-Manager
     */
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Retrieve an Instance of the UserManager.
     * @return UserManager instance.
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Replace the UserManager.
     * @param userManager the new UserManager.
     */
    @Deprecated
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Retrieve an Instance of the PacketManager.
     * @return PacketManager instance.
     */
    public PacketManager getPacketManager() {
        return packetManager;
    }

    /**
     * Replace the PacketManager.
     * @param packetManager the new PacketManager.
     */
    @Deprecated
    public void setUserManager(PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    /**
     * Retrieve an Instance of GSON.
     * @return GSON Instance.
     */
    public Gson getGson() {
        return gson;
    }
}
