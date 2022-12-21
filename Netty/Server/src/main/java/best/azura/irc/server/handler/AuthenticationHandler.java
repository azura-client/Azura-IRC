package best.azura.irc.server.handler;

import best.azura.irc.server.Controller;
import best.azura.irc.server.packets.client.UserAuthenticationPacket;
import best.azura.irc.utils.PacketUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class AuthenticationHandler extends ChannelInboundHandlerAdapter {

    Controller controller;
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
            ctx.pipeline().addLast(new ClientHandler(controller, UUID.randomUUID(), userAuthenticationPacket.getUsername(), userAuthenticationPacket.getRank()));
        } else {
            // handle.
        }
    }
}
