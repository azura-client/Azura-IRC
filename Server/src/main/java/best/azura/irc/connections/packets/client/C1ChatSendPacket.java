package best.azura.irc.connections.packets.client;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.Message;
import best.azura.irc.entities.User;

public class C1ChatSendPacket extends Packet {

    public static int packetId = 0;

    public C1ChatSendPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public C1ChatSendPacket(User user) {
        super(user);
        Id = packetId;
    }

    public Message getMessage() {
        if (!getContent().has("message"))
            return null;

        int channelId = getContent().has("channel") ? getContent().get("channel").getAsInt() : 0;

        return new Message(getUser(), getContent().get("message").getAsString(), channelId, false);
    }
}
