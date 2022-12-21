package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import best.azura.irc.server.session.Session;

@PacketInfo(id = 5, typ = PacketTyp.CLIENT)
public class TransactionConfirmationPacket implements IPacket {

    String transactionId;

    @Override
    public void process(Session session) {

    }
}
