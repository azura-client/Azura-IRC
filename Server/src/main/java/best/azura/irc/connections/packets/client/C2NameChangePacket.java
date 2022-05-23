package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class C2NameChangePacket extends Packet {

    public static int packetId = 0;

    public C2NameChangePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C2NameChangePacket(User user) {
        super(user);
        Id = packetId;
    }

    public String getName() {
        if (getContent() == null || !getContent().has("name"))
            return null;

        return getContent().get("name").getAsString();
    }
}
