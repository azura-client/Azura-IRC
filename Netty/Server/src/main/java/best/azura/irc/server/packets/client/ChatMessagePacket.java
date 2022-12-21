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
@PacketInfo(id = 2, typ = PacketTyp.CLIENT)
public class ChatMessagePacket implements IPacket {

    private String messageContent;

    @Override
    public void process(Session session) {
    }
}
