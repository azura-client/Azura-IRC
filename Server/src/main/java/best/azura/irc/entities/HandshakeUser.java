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
public class HandshakeUser {

    // Instance of the established Socket Connection
    Socket clientSocket;

    // Print-Stream of the Client to send data.
    PrintStream printStream;

    // AES-Encryption Key.
    Key aesKey;

    // Current InputScanner.
    Scanner inputScanner;

    // Connection Time.
    long connectedTime;

    /**
     * Constructor to create a new User to manage.
     * @param clientSocket Instance of the Socket Connection.
     * @param aesKey AES-Encryption Key of the Session.
     * @param inputScanner Input Scanner.
     */
    public HandshakeUser(Socket clientSocket, Key aesKey, Scanner inputScanner) {
        this.clientSocket = clientSocket;
        Main.getInstance().getLogger().debug("Set ClientSocket");
        this.aesKey = aesKey;
        Main.getInstance().getLogger().debug("Set AES-Key");
        this.inputScanner = inputScanner;
        Main.getInstance().getLogger().debug("Set Input-Stream");

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
     * Get the AES Encryption Key.
     * @return AES-Key.
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
