package best.azura.irc.connections.handler;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.connections.packets.client.C7HandshakeRequest;
import best.azura.irc.connections.packets.server.S0LoginResponsePacket;
import best.azura.irc.connections.packets.server.S7HandshakeResponse;
import best.azura.irc.entities.HandshakeUser;
import best.azura.irc.main.Main;
import best.azura.irc.server.Server;
import best.azura.irc.utils.AESUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;

public class ConnectionHandler {

    // Threads for the single actions.
    private Thread handlerThread;

    // Instance of the Server class.
    private final Server server;

    /**
     * Constructor for the Connection-Handler
     *
     * @param server Server class instance.
     */
    public ConnectionHandler(Server server) {
        this.server = server;
    }

    /**
     * Call to start the Handlers.
     */
    public void startHandler() {

        // Init the Handler-Thread.
        handlerThread = new Thread(() -> {

            // Check if the Server is bound to the Port.
            while (!server.getServerSocket().isClosed()) {

                Main.getInstance().getLogger().debug("Running.");

                // Init variable clientSocket.
                Socket clientSocket = null;

                // Try accepting an incoming Connection.
                try {
                    clientSocket = server.getServerSocket().accept();
                    Main.getInstance().getLogger().debug("Accepted a ClientSocket.");
                } catch (Exception ex) {
                    Main.getInstance().getLogger().info("Failed accepting Incoming Connection.");
                }

                // Check if it's null or not.
                if (clientSocket == null) continue;

                // Init variable inputScanner.
                Scanner inputScanner = null;

                // Scanner to get the first line with credentials.
                try {
                    inputScanner = new Scanner(clientSocket.getInputStream());
                    Main.getInstance().getLogger().debug("Created Input-Scanner.");
                } catch (IOException e) {
                    Main.getInstance().getLogger().info("Failed creating Input Scanner.");
                }

                // Check if it's null or not.
                if (inputScanner == null) continue;

                if (!inputScanner.hasNextLine()) {
                    Main.getInstance().getLogger().debug("No Line so disconnected.");
                    // Notify User.
                    try {
                        server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S0LoginResponsePacket(false));
                    } catch (Exception ignore) {
                    }

                    try {
                        inputScanner.close();
                        clientSocket.close();
                    } catch (IOException ignore) {
                    }
                }
                String content = null;

                try {
                    content = inputScanner.nextLine();
                    Main.getInstance().getLogger().debug("Getting Content.");

                    if (content == null) {
                        Main.getInstance().getLogger().info("Invalid Content: (" + clientSocket.getInetAddress().getHostAddress() + ")");
                        // Notify User.
                        try {
                            server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S0LoginResponsePacket(false));
                        } catch (Exception ignore) {
                        }

                        // If it isn't successful disconnect.
                        try {
                            inputScanner.close();
                            clientSocket.close();
                        } catch (Exception ignore) {
                        }
                        continue;
                    }

                    // Parse.
                    JsonElement jsonElement = JsonParser.parseString(content);

                    if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("id") && jsonElement.getAsJsonObject().has("content")) {
                        Main.getInstance().getLogger().debug("Valid JSON.\n" + content);
                        // Get the Login Packet.
                        Packet packet = server.getPacketManager().getPacket(jsonElement.getAsJsonObject(), null);
                        Main.getInstance().getLogger().debug("Tried to resolve Packet.");

                        if (packet instanceof C7HandshakeRequest c7HandshakeRequest) {

                            Main.getInstance().getLogger().debug("Got valid Handshake Packet.");

                            if (c7HandshakeRequest.isValid()) {

                                Main.getInstance().getLogger().debug("Got valid format.");
                                Key aesKey;

                                try {
                                    aesKey = AESUtil.generateSecureKey();

                                    HandshakeUser handshakeUser = new HandshakeUser(clientSocket, aesKey, inputScanner);

                                    S7HandshakeResponse s7HandshakeResponse = new S7HandshakeResponse(Base64.getEncoder().encodeToString(aesKey.getEncoded()), true, "");
                                    server.getPacketManager().sendPacket(server, handshakeUser.getPrintStream(), s7HandshakeResponse);
                                    Main.getInstance().getLogger().info("User Handshake successful! (" + clientSocket.getInetAddress().getHostAddress() + ")");

                                    new LoginHandler(handshakeUser, server).startHandler();
                                } catch (Exception exception) {
                                    S7HandshakeResponse s7HandshakeResponse = new S7HandshakeResponse(null, false, exception.getMessage());

                                    server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), s7HandshakeResponse);
                                    Main.getInstance().getLogger().info("User Handshake failed! (" + clientSocket.getInetAddress().getHostAddress() + ")");
                                }
                            } else {
                                Main.getInstance().getLogger().info("User Handshake failed! (" + clientSocket.getInetAddress().getHostAddress() + ", INVALID FORMAT)");
                                // Notify User.
                                try {
                                    server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S7HandshakeResponse(null, false, "Invalid Format."));
                                } catch (Exception ignore) {
                                }

                                // If it isn't successful disconnect.
                                try {
                                    inputScanner.close();
                                    clientSocket.close();
                                } catch (Exception ignore) {
                                }
                            }
                        } else {
                            Main.getInstance().getLogger().info("Received wrong Packet on connection! (" + clientSocket.getInetAddress().getHostAddress() + ")");
                            // Notify User.
                            try {
                                server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S7HandshakeResponse(null, false, "Wrong Packet."));
                            } catch (Exception ignore) {
                            }

                            try {
                                inputScanner.close();
                                clientSocket.close();
                            } catch (IOException ignore) {
                            }
                        }

                    } else {
                        Main.getInstance().getLogger().info("Received invalid Json! (" + clientSocket.getInetAddress().getHostAddress() + ")\nCotent:\n" + content);
                        // Notify User.
                        try {
                            server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S7HandshakeResponse(null, false, "Invalid JSON"));
                        } catch (Exception ignore) {
                        }

                        try {
                            inputScanner.close();
                            clientSocket.close();
                        } catch (IOException ignore) {
                        }
                    }
                } catch (Exception exception) {
                    if (exception instanceof IllegalStateException) {
                        Main.getInstance().getLogger().error("Couldn't handle connection! (" + clientSocket.getInetAddress().getHostAddress() + ", INPUT CLOSED)");
                    } else if (exception instanceof JsonSyntaxException) {
                        Main.getInstance().getLogger().error("Couldn't handle connection! (" + clientSocket.getInetAddress().getHostAddress() + ", NO JSON)\nContent:\n" + (content != null ? content : "NULL"));
                    } else {
                        Main.getInstance().getLogger().error("Couldn't handle connection!(" + clientSocket.getInetAddress().getHostAddress() + ")\nContent:\n" + (content != null ? content : "NULL"), exception);
                    }

                    // Notify User.
                    try {
                        server.getPacketManager().sendPacket(server, new PrintStream(clientSocket.getOutputStream()), new S7HandshakeResponse(null, false, exception.getMessage()));
                    } catch (Exception ignore) {
                    }

                    try {
                        inputScanner.close();
                        clientSocket.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            Main.getInstance().getLogger().warn("Connection Handler Thread stopped!");

            // Stop Handler.
            ////stopHandler();
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

        // Stop all MessageHandler.
        server.getUserManager().getUserMessageMapList()
                .forEach((user, messageHandler) -> messageHandler.stopHandler());
    }

}
