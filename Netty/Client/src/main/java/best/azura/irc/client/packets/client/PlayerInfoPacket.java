package best.azura.irc.client.packets.client;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.client.packets.base.PacketInfo;
import best.azura.irc.client.packets.base.PacketTyp;
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
    public void process() {
    }
}
