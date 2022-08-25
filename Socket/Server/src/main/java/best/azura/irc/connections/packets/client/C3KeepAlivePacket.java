package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class C3KeepAlivePacket extends Packet {

    public static int packetId = 0;

    public C3KeepAlivePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C3KeepAlivePacket() {
        super((User) null);
        Id = packetId;
    }
}
