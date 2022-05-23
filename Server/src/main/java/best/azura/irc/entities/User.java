package best.azura.irc.entities;

import best.azura.irc.main.Main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

/**
 * Class to save Data about connected Users.
 */
public class User {

    // Instance of the established Socket Connection
    Socket clientSocket;

    // Print-Stream of the Client to send data.
    PrintStream printStream;

    // Client Username.
    String username;

    // Current in-game Username.
    String minecraftName;

    // Current Ip-Address.
    String ip;

    // Current InputScanner.
    Scanner inputScanner;

    // AES-Encryption Key.
    Key aesKey;

    // Connection Time.
    long connectedTime;

    /**
     * Constructor to create a new User to manage.
     * @param clientSocket Instance of the Socket Connection.
     * @param userName Client Username.
     * @param minecraftName Minecraft Username.
     * @param aesKey AES-Encryption Key.
     * @param inputScanner Input Scanner.
     */
    public User(Socket clientSocket, String userName, String minecraftName, Key aesKey, Scanner inputScanner) {
        Main.getInstance().getLogger().debug("Creating new User Instance");
        this.clientSocket = clientSocket;
        Main.getInstance().getLogger().debug("Set ClientSocket");
        this.username = userName;
        Main.getInstance().getLogger().debug("Set Username (" + userName + ")");
        this.minecraftName = minecraftName;
        Main.getInstance().getLogger().debug("Set Client-Rank");
        this.inputScanner = inputScanner;
        Main.getInstance().getLogger().debug("Set Input-Stream");
        this.aesKey = aesKey;
        Main.getInstance().getLogger().debug("Set Encryption Key");
        this.ip = clientSocket.getInetAddress().getHostAddress();
        Main.getInstance().getLogger().debug("Set IP");
        connectedTime = System.currentTimeMillis();
        Main.getInstance().getLogger().debug("Set connected Time");
    }

    /**
     * Retrieve the Client-Socket of the Users.
     * @return user client socket.
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Replace the current Client-Socket with another one.
     * @param clientSocket new Client-Socket.
     */
    @Deprecated
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Retrieve or create a PrintStream to send data to a User.
     * @return PrintStream of the User.
     * @throws IOException If the OutputStream is closed.
     */
    public PrintStream getPrintStream() throws IOException {
        return printStream != null ? printStream : setPrintStream(new PrintStream(clientSocket.getOutputStream()));
    }

    /**
     * Replace the current PrintStream.
     * @param printStream the new PrintStream
     * @return the PrintStream.
     */
    public PrintStream setPrintStream(PrintStream printStream) {
        return this.printStream = printStream;
    }

    /**
     * Retrieve Username.
     * @return actual username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Replace the username.
     * @param username new username.
     */
    @Deprecated
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieve the Minecraft Username.
     * @return Minecraft username.
     */
    public String getMinecraftName() {
        return minecraftName;
    }

    /**
     * Replace the Minecraft Name.
     * @param minecraftName new Minecraft Name.
     */
    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    /**
     * Retrieve the current IP-Address.
     * @return the IP-Address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Change the save IP of a User.
     * @param ip new IP.
     */
    @Deprecated
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Get current Connection Time.
     * @return connection Time.
     */
    public long getConnectedTime() {
        return connectedTime;
    }

    /**
     * Change the saved connection Time.
     * @param connectedTime new connection Time.
     */
    @Deprecated
    public void setConnectedTime(long connectedTime) {
        this.connectedTime = connectedTime;
    }

    /**
     * Retrieve the AES Encryption Key of the User.
     * @return the AES Key.
     */
    public Key getAESKey() {
        return aesKey;
    }

    /**
     * Retrieve an Instance of the Input Scanner.
     * @return the Input Scanner.
     */
    public Scanner getInputScanner() {
        return inputScanner;
    }
}
