package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import best.azura.irc.server.session.Session;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
@PacketInfo(id = 0, typ = PacketTyp.CLIENT)
public class UserAuthenticationPacket implements IPacket {

    String username;
    String password;
    String salt;

    int rank;

    @Override
    public void process(Session session) {

    }
}
