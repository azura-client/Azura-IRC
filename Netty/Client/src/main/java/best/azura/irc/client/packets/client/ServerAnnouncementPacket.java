package best.azura.irc.client.packets.client;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.client.packets.base.PacketInfo;
import best.azura.irc.client.packets.base.PacketTyp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PacketInfo(id = 5, typ = PacketTyp.SERVER)
public class ServerAnnouncementPacket implements IPacket {

    private String message;
    private UUID sender;

    @Override
    public void process() {
    }
}

