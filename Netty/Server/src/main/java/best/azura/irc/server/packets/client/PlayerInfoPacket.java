package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import best.azura.irc.server.session.Session;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@PacketInfo(id = 1, typ = PacketTyp.CLIENT)
public class PlayerInfoPacket implements IPacket {

    private String ign;
    private String server;
    private long playtime;
    private String[] activeModules;

    @Override
    public void process(Session session) {
    }
}
