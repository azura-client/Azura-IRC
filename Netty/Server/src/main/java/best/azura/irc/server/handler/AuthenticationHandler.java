package best.azura.irc.server.handler;

import best.azura.irc.server.Controller;
import best.azura.irc.server.packets.client.UserAuthenticationPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class AuthenticationHandler extends ChannelInboundHandlerAdapter {

    Controller controller;
    boolean authed;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        log.info("New connection from " + ctx.channel().remoteAddress());
        controller.addChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Received data -> " + msg);

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
