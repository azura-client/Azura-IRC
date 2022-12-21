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
@PacketInfo(id = 3, typ = PacketTyp.CLIENT)
public class CommandMessagePacket implements IPacket {

    String command;
    String[] args;

    @Override
    public void process() {
    }
}
