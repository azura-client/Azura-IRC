package best.azura.irc.client.handler;

import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class PacketEncoder extends MessageToByteEncoder<IPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IPacket msg, ByteBuf out) throws Exception {
        out.writeBytes(PacketUtil.toJSON(msg).getBytes(StandardCharsets.UTF_8));
        log.info("Sent packet: " + msg);
    }
}
