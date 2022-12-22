import best.azura.irc.client.Client;

public class Main {

    static Client client;

    public static void main(String[] args) {
        client = new Client("127.0.0.1", 6969);
        client.connect();
    }

}
