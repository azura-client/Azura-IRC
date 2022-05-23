package best.azura.irc.connections.handler;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.connections.packets.client.C0LoginRequestPacket;
import best.azura.irc.connections.packets.server.S0LoginResponsePacket;
import best.azura.irc.entities.HandshakeUser;
import best.azura.irc.entities.User;
import best.azura.irc.main.Main;
import best.azura.irc.server.Server;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginHandler {

    // Instance of the User.
    HandshakeUser user;

    // Instance of the Server.
    Server server;

    // The actual Thread.
    Thread handlerThread;

    /**
     * Constructor for the Connection-Handler
     *
     * @param user   User class instance.
     * @param server Server class instance.
     */
    public LoginHandler(HandshakeUser user, Server server) {
        this.user = user;
        this.server = server;
    }

    /**
     * Call to start the Handlers.
     */
    public void startHandler() {

        // Init the Handler-Thread.
        handlerThread = new Thread(() -> {
            // Check if the User is still connected to the Socket.
            if (!user.getClientSocket().isConnected()) return;

            // Create the variable for the inputScanner.
            Scanner inputScanner = user.getInputScanner();

            // Check if there are still messages from the input and if the Server is still running and if the user is still connected.
            while (inputScanner != null && inputScanner.hasNextLine() && user.getClientSocket() != null && user.getClientSocket().isConnected() && !server.getServerSocket().isClosed()) {

                Main.getInstance().getLogger().debug("Running.");


                if (!inputScanner.hasNextLine()) {
                    Main.getInstance().getLogger().debug("No Line so disconnected.");
                    // Notify User.
                    try {
                        server.getPacketManager().sendPacket(server, new PrintStream(user.getClientSocket().getOutputStream()), new S0LoginResponsePacket(false));
                    } catch (Exception ignore) {
                    }

                    try {
                        inputScanner.close();
                        user.getClientSocket().close();
                    } catch (IOException ignore) {
                    }
                }

                String content = null;
                try {
                    content = inputScanner.nextLine();

                    Main.getInstance().getLogger().debug("Getting Content.");

                    // Parse.
                    JsonElement jsonElement = JsonParser.parseString(content);

                    if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("id") && jsonElement.getAsJsonObject().has("content")) {
                        Main.getInstance().getLogger().debug("Valid JSON.");
                        // Get the Login Packet.
                        Packet packet = server.getPacketManager().getPacket(jsonElement.getAsJsonObject(), user);
                        Main.getInstance().getLogger().debug("Tried to resolve Packet.");

                        if (packet instanceof C0LoginRequestPacket c0LoginRequestPacket) {

                            Main.getInstance().getLogger().debug("Got valid Login Packet.");

                            if (c0LoginRequestPacket.isValidFormat()) {

                                Main.getInstance().getLogger().debug("Got valid format.");

                                if (!c0LoginRequestPacket.isIntent()) {
                                    Main.getInstance().getLogger().debug("None Intent.");
                                    // Make a UserLogin Request and get the Response.
                                    if (server.getUserManager().getUserList().stream().anyMatch(user -> user != null && user.getUsername().equalsIgnoreCase(c0LoginRequestPacket.getUsername()))) {
                                        Main.getInstance().getLogger().debug("Got more then on Session.");

                                        ArrayList<User> users = server.getUserManager().getUserList();

                                        for (User user : users) {
                                            if (user == null || !user.getUsername().equalsIgnoreCase(c0LoginRequestPacket.getUsername()))
                                                continue;

                                            server.getUserManager().disconnectUser(user, false);
                                            Main.getInstance().getLogger().info("Disconnected User: " + user.getUsername());
                                        }

                                        server.getUserManager().setUserList(users);
                                        Main.getInstance().getLogger().debug("Disconnected all other Sessions.");
                                    }

                                    Main.getInstance().getLogger().debug("Trying to create User.");
                                    // Create the User.
                                    User newUser = new User(user.getClientSocket(), c0LoginRequestPacket.getUsername(), "", user.getAESKey(), inputScanner);
                                    newUser.setPrintStream(user.getPrintStream());
                                    Main.getInstance().getLogger().debug("Created User.");

                                    // Add him to the List.
                                    server.getUserManager().addUser(newUser);

                                    Main.getInstance().getLogger().info("User connected: " + newUser.getUsername());

                                    server.getPacketManager().sendPacket(server, newUser, new S0LoginResponsePacket(true));
                                    Main.getInstance().getLogger().debug("Send login success Packet.");
                                } else {
                                    // TODO mega cool and performant Intent Login thingy, and check if the Product is added
                                    Main.getInstance().getLogger().info("Someone tried to use the Intent Auth! (" + user.getClientSocket().getInetAddress().getHostAddress() + ")");
                                    // Notify User.
                                    try {
                                        server.getPacketManager().sendPacket(server, user, new S0LoginResponsePacket(false));
                                    } catch (Exception ignore) {
                                    }

                                    // If it isn't successful disconnect.
                                    try {
                                        inputScanner.close();
                                        user.getClientSocket().close();
                                        break;
                                    } catch (Exception ignore) {
                                    }
                                }
                            } else {
                                Main.getInstance().getLogger().info("User connection failed: " + c0LoginRequestPacket.getUsername() + " (INVALID FORMAT)");
                                // Notify User.
                                try {
                                    server.getPacketManager().sendPacket(server, user, new S0LoginResponsePacket(false));
                                } catch (Exception ignore) {
                                }

                                // If it isn't successful disconnect.
                                try {
                                    inputScanner.close();
                                    user.getClientSocket().close();
                                    break;
                                } catch (Exception ignore) {
                                }
                            }
                        } else {
                            Main.getInstance().getLogger().info("Received wrong Packet on connection! (" + user.getClientSocket().getInetAddress().getHostAddress() + ")");
                            // Notify User.
                            try {
                                server.getPacketManager().sendPacket(server, user, new S0LoginResponsePacket(false));
                            } catch (Exception ignore) {
                            }

                            try {
                                inputScanner.close();
                                user.getClientSocket().close();
                                break;
                            } catch (IOException ignore) {
                            }
                        }

                    } else {
                        Main.getInstance().getLogger().info("Received invalid Json! (" + user.getClientSocket().getInetAddress().getHostAddress() + ")\nCotent:\n" + content);
                        // Notify User.
                        try {
                            server.getPacketManager().sendPacket(server, user, new S0LoginResponsePacket(false));
                        } catch (Exception ignore) {
                        }

                        try {
                            inputScanner.close();
                            user.getClientSocket().close();
                            break;
                        } catch (IOException ignore) {
                        }
                    }
                } catch (Exception exception) {
                    if (exception instanceof IllegalStateException) {
                        Main.getInstance().getLogger().error("Couldn't handle connection! (" + user.getClientSocket().getInetAddress().getHostAddress() + ", INPUT CLOSED)");
                    } else if (exception instanceof JsonSyntaxException) {
                        Main.getInstance().getLogger().error("Couldn't handle connection! (" + user.getClientSocket().getInetAddress().getHostAddress() + ", NO JSON)\nContent:\n" + (content != null ? content : "NULL"));
                    } else {
                        Main.getInstance().getLogger().error("Couldn't handle connection!(" + user.getClientSocket().getInetAddress().getHostAddress() + ")\nContent:\n" + (content != null ? content : "NULL"), exception);
                    }

                    // Notify User.
                    try {
                        server.getPacketManager().sendPacket(server, user, new S0LoginResponsePacket(false));
                    } catch (Exception ignore) {
                    }

                    try {
                        inputScanner.close();
                        user.getClientSocket().close();
                        break;
                    } catch (IOException ignore) {
                    }
                }
            }

            Main.getInstance().getLogger().warn("Connection Handler Thread stopped!");

            // Stop Handler.
            stopHandler();
        });

        // Start the Handler Thread.
        handlerThread.start();
    }

    /**
     * Stop the current Handler-Thread.
     */
    public void stopHandler() {

        // Interrupt it.
        handlerThread.interrupt();
    }

}
