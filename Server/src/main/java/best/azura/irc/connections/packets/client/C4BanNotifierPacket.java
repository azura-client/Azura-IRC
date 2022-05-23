package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class C4BanNotifierPacket extends Packet {

    public static int packetId = 0;

    public C4BanNotifierPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C4BanNotifierPacket(User user) {
        super(user);
        Id = packetId;
    }

    public String getServer() {
        if (getContent() == null || !getContent().has("server"))
            return null;

        return getContent().get("server").getAsString();
    }

    public long getPlaytime() {
        if (getContent() == null || !getContent().has("playtime"))
            return 0L;

        return getContent().get("playtime").getAsLong();
    }

}
