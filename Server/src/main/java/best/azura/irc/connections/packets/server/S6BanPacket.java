package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;

public class S6BanPacket extends Packet {

    public static int packetId = 0;

    public S6BanPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S6BanPacket(User user) {
        super(user);
        Id = packetId;
    }
}
