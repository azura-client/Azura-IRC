package best.azura.irc.client.packets.client;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.client.packets.base.PacketInfo;
import best.azura.irc.client.packets.base.PacketTyp;
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
    public void process() {
    }
}
