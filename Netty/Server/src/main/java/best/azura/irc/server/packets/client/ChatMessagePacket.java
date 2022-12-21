package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import best.azura.irc.server.session.Session;

@PacketInfo(id = 2, typ = PacketTyp.CLIENT)
public class ChatMessagePacket implements IPacket {

    @Override
    public void process(Session session) {

    }
}
