package best.azura.irc.client.handler;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class PacketEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof IPacket) {
            IPacket packet = (IPacket) msg;
            out.writeBytes(PacketUtil.toJSON(packet).getBytes(StandardCharsets.UTF_8));
        } else {
            log.error("Invalid message going out!");
        }
    }
}
