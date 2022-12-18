package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;

@PacketInfo(id = 5, typ = PacketTyp.CLIENT)
public class TransactionConfirmationPacket implements IPacket {

    String transactionId;

    @Override
    public void read(Object data) {

    }
}
