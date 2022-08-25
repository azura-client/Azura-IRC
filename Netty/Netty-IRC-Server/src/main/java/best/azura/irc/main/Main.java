package best.azura.irc.main;

import best.azura.irc.server.Server;

public class Main {

    private static Server server;

    public static void main(String[] args) throws Exception {
        server = new Server(6969);
        server.start();
    }

}
