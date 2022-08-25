package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class C5EmotePacket extends Packet {

    public static int packetId = 0;

    public C5EmotePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C5EmotePacket(User user) {
        super(user);
        Id = packetId;
    }

    public String getEmote() {
        if (getContent() == null || !getContent().has("emote"))
            return null;

        return getContent().get("emote").getAsString();
    }

    public String getServer() {
        if (getContent() == null || !getContent().has("server"))
            return null;

        return getContent().get("server").getAsString();
    }
}
