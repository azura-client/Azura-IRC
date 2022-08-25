package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;

@PacketInfo(id = 3, typ = PacketTyp.CLIENT)
public class CommandMessagePacket implements IPacket {
    @Override
    public void read(Object data) {

    }
}
