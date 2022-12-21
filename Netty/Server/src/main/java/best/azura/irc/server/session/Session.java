package best.azura.irc.server.session;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.utils.PacketUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class Session {

    @Getter(AccessLevel.PUBLIC)
    private volatile ChannelHandlerContext ctx;
    @Getter(AccessLevel.PUBLIC)
    private volatile UUID uuid;
    @Getter(AccessLevel.PUBLIC)
    private volatile String username;
    @Getter(AccessLevel.PUBLIC)
    private volatile Rank rank;

    public void send(IPacket packet) {
        ctx.writeAndFlush(packet);
    }

    public void terminate() {
        ctx.close();
    }

}
