package best.azura.irc.server.handler;

import best.azura.irc.server.packets.client.UserAuthenticationPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthenticationHandler extends ChannelInboundHandlerAdapter {

    boolean authed;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (authed) {
            ctx.fireChannelRead(msg);
            return;
        }

        if (msg instanceof UserAuthenticationPacket userAuthenticationPacket) {
            // do auth.
            authed = true;
            ctx.pipeline().addLast("stringDecoder", new PacketHandler());
        } else {
            // handle.
        }
    }
}
