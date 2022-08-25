package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import com.google.gson.JsonObject;

@PacketInfo(id = 0, typ = PacketTyp.CLIENT)
public class PlayerInfoPacket implements IPacket {
    JsonObject data;

    @Override
    public void read(Object data) {

    }

    @Override
    public Object getData() {
        return data;
    }
}
