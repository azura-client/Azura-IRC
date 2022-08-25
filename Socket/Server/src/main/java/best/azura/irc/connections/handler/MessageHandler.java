package best.azura.irc.connections.handler;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import best.azura.irc.main.Main;
import best.azura.irc.server.Server;
import best.azura.irc.utils.AESUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Scanner;

public class MessageHandler {

    // Instance of the User.
    User user;

    // Instance of the Server.
    Server server;

    // The actual Thread.
    Thread handlerThread;

    /**
     * Constructor for the MessageHandler.
     *
     * @param user   the "owner" of the MessageHandler.
     * @param server an instance of the Server.
     */
    public MessageHandler(User user, Server server) {
        this.user = user;
        this.server = server;
    }

    public void startHandler() {

        // Create HandlerThread.
        handlerThread = new Thread(() -> {

            try {
                // Check if the User is still connected to the Socket.
                if (!user.getClientSocket().isConnected()) return;

                // Create the variable for the inputScanner.
                Scanner inputScanner = user.getInputScanner();

                // Check if there are still messages from the input and if the Server is still running and if the user is still connected.
                while (inputScanner != null && inputScanner.hasNextLine() && user.getClientSocket() != null && user.getClientSocket().isConnected() && !server.getServerSocket().isClosed()) {
                    String content = inputScanner.nextLine();

                    JsonElement jsonElement = JsonParser.parseString(content);

                    if (jsonElement != null && jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("id")) {
                        Packet packet = server.getPacketManager().getPacketById(jsonElement.getAsJsonObject().get("id").getAsInt());

                        if (packet != null) {
                            if (packet.isEncrypt()) {
                                String decrypted = AESUtil.decrypt(jsonElement.getAsJsonObject().get("content").getAsString(), user.getAESKey());

                                if (decrypted != null) {
                                    JsonElement jsonElement1 = JsonParser.parseString(decrypted);
                                    if (jsonElement1 != null && jsonElement1.isJsonObject())
                                        packet.setContent(jsonElement1.getAsJsonObject());
                                    else continue;
                                } else continue;
                            } else {
                                packet.setContent(jsonElement.getAsJsonObject().getAsJsonObject("content"));
                            }

                            packet.setUser(user);
                            server.getPacketHandler().sendPacket(packet);
                        } else {
                            Main.getInstance().getLogger().warn("Ignoring Invalid Packet with the Content:\n" + content);
                        }
                    } else {
                        Main.getInstance().getLogger().warn("Ignoring Data with the Content:\n" + content);
                    }
                }
            } catch (Exception exception) {
                Main.getInstance().getLogger().error("Issue while trying to read Input-stream for " + user.getUsername() + "!", exception);
            }

            // Close inputScanner to prevent memory-leak.
            if (user.getInputScanner() != null) user.getInputScanner().close();

            // Disconnect User.
            server.getUserManager().disconnectUser(user, true);
        });

        handlerThread.start();
    }

    /**
     * Stop the current Handler-Thread.
     */
    public void stopHandler() {

        // Interrupt it.
        handlerThread.interrupt();

        // Remove the MessageHandler form the MapList.
        server.getUserManager().getUserMessageMapList().remove(user, this);
    }

}
