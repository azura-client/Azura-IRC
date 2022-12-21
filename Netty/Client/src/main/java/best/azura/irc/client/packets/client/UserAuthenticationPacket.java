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
@PacketInfo(id = 0, typ = PacketTyp.CLIENT)
public class UserAuthenticationPacket implements IPacket {

    String username;
    String password;
    String salt;

    int rank;

    @Override
    public void process() {
    }
}
