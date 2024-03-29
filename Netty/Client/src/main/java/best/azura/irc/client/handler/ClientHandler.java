package best.azura.irc.client.handler;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.client.packets.client.UserAuthenticationPacket;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelFuture channelFuture = ctx.writeAndFlush(new UserAuthenticationPacket("baller", "bals", "SEXS", 2));
        try {
            log.info("Got Channel Id " + channelFuture.sync().channel().id());
        } catch (Exception exception) {
            log.error("Channel activation failed.", exception);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        if (msg instanceof IPacket) {
            ((IPacket) msg).process();
        }

        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception caught!", cause);
        ctx.close();
    }
}
