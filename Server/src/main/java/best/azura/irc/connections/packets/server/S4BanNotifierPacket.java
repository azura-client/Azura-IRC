package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S4BanNotifierPacket extends Packet {

    public static int packetId = 0;

    public S4BanNotifierPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S4BanNotifierPacket(User user, String server) {
        super(user);
        Id = packetId;

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", user.getUsername());
        jsonObject.addProperty("server", server);

        setContent(jsonObject);
    }

}
