package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S2NameChangePacket extends Packet {

    public static int packetId = 0;

    public S2NameChangePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S2NameChangePacket(User user) {
        super(user);
        Id = packetId;

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", user.getUsername());
        jsonObject.addProperty("ingame", user.getMinecraftName());

        setContent(jsonObject);
    }
}
