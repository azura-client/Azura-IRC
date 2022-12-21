package best.azura.irc.server.handler;

import best.azura.irc.server.Controller;
import best.azura.irc.server.session.InMemoryRepository;
import best.azura.irc.server.session.Rank;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Controller controller;

    private UUID uuid;

    private String username;

    private int rank;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        try{
            controller.accept(InMemoryRepository.buildSession(ctx, uuid, username, Rank.fromId(rank)));
            ctx.writeAndFlush(String.format("Welcome %s", uuid));
        }catch (Exception e) {
            log.error("Unexpected exception", e.getMessage());
            ctx.writeAndFlush(e.getMessage());
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        controller.close(uuid);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            controller.receiveMessage(uuid);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught ", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
