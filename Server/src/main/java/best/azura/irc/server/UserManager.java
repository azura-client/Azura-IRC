package best.azura.irc.server;

import best.azura.irc.connections.handler.MessageHandler;
import best.azura.irc.entities.Message;
import best.azura.irc.entities.User;
import best.azura.irc.main.Main;
import best.azura.irc.utils.Crypter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

    // The list of Users.
    private ArrayList<User> userList;

    // The Map with the User and his MessageHandler.
    private final HashMap<User, MessageHandler> userMessageMapList;

    /**
     * Constructor to initialize the List and Map.
     */
    public UserManager() {
        userList = new ArrayList<>();
        userMessageMapList = new HashMap<>();
    }

    /**
     * Add a User to the List and create a MessageHandler for him.
     * @param user the User to handle.
     */
    public void addUser(User user) {

        // Check if the User is already added to the List.
        if (!getUserList().contains(user)) {
            getUserList().add(user);
        }

        // Check if the User is already added to the MessageMap.
        if (!getUserMessageMapList().containsKey(user)) {

            // Create MessageHandler.
            MessageHandler messageHandler = new MessageHandler(user, Main.getInstance().getServer());

            // Add MessageHandler and User to MapList.
            getUserMessageMapList().put(user, messageHandler);

            // Start MessageHandler.
            messageHandler.startHandler();
        }

        // Send him a Welcome Message.
        // sendConsoleMessage(user, "Welcome!");
    }

    /**
     * Send a Message to a specified User.
     * @param user the receiver of the Message.
     * @param message the content of the Message.
     */
    public void sendMessage(User user, Message message) {
        try {
            user.getPrintStream().println(Crypter.encode(message.getFormattedMessage()));
        } catch (Exception ex) {
            Main.getInstance().getLogger().info("Couldn't send a Message to the User " + user.getUsername() + "!");
        }
    }

    /**
     * Send a Message to all Users.
     * @param message the content of the Message.
     */
    public void sendMessageToAll(Message message) {
        getUserList().forEach(user -> sendMessage(user, message));
    }

    /**
     * Disconnect a User from the Server (the proper way).
     * @param user the User.
     * @param remove should it remove the User from the current List.
     */
    public void disconnectUser(User user, boolean remove) {

        if (user == null) return;

        Main.getInstance().getLogger().info("Disconnecting User: " + user.getUsername());

        // Check if null or not registered or not connected.
        if ((!getUserList().contains(user) && !remove) || !user.getClientSocket().isConnected()) return;

        new Thread(() -> {
            Main.getInstance().getLogger().info("Trying to close Input-Scanner User: " + user.getUsername());

            // Close Scanner to prevent memory leak.
            if (user.getInputScanner() != null) user.getInputScanner().close();

            Main.getInstance().getLogger().info("Closed Input-Scanner User: " + user.getUsername());
        }).start();

        // Check if the User has a MessageHandler.
        if (getUserMessageMapList().containsKey(user)) {
            Main.getInstance().getLogger().info("Found MessageHandler User: " + user.getUsername());

            // Close the Handler.
            getUserMessageMapList().get(user).stopHandler();

            Main.getInstance().getLogger().info("Closed MessageHandler User: " + user.getUsername());

            // Remove from the List.
            getUserMessageMapList().remove(user);
            Main.getInstance().getLogger().info("Removed MessageHandler User: " + user.getUsername());
        }

        // Catch if something happens.
        try {

            // Close PrintStream.
            user.getPrintStream().close();
            Main.getInstance().getLogger().info("Closed Print-Stream User: " + user.getUsername());

            // Close Client connection.
            user.getClientSocket().close();
            Main.getInstance().getLogger().info("Closed Socket User: " + user.getUsername());
        } catch (Exception ignore) {}

        if(remove) removeUser(user);
    }

    /**
     * Remove User from list.
     * @param user the User.
     */
    private void removeUser(User user) {
        try {
            getUserList().remove(user);
            Main.getInstance().getLogger().info("Disconnected User: " + user.getUsername());
        } catch (Exception ignored) {
            removeUser(user);
        }
    }

    /**
     * Retrieve current UserList.
     * @return the UserList.
     */
    public ArrayList<User> getUserList() {
        return userList;
    }

    /**
     * Update User-List.
     *
     * @param users new List with Users.
     */
    public void setUserList(ArrayList<User> users) {
        userList = users;
    }

    /**
     * Retrieve current UserMessageMapList.
     * @return the UserMessageMapList.
     */
    public HashMap<User, MessageHandler> getUserMessageMapList() {
        return userMessageMapList;
    }
}
