package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class C6ClientInfoPacket extends Packet {

    public static int packetId = 0;

    public C6ClientInfoPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C6ClientInfoPacket(User user) {
        super(user);
        Id = packetId;
    }
}
