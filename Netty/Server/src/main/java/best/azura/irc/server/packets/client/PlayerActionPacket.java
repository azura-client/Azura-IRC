package best.azura.irc.server.packets.client;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import best.azura.irc.server.packets.base.PacketTyp;
import best.azura.irc.server.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@PacketInfo(id = 4, typ = PacketTyp.CLIENT)
public class PlayerActionPacket implements IPacket {

    private String action;
    @Override
    public void process(Session session) {
    }
}
