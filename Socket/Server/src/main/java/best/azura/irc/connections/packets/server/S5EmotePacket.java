package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S5EmotePacket extends Packet {

    public static int packetId = 0;

    public S5EmotePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S5EmotePacket(User user, String emote, String server) {
        super(user);
        Id = packetId;

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", user.getUsername());
        jsonObject.addProperty("emote", emote);
        jsonObject.addProperty("server", server);

        setContent(jsonObject);
    }

}
