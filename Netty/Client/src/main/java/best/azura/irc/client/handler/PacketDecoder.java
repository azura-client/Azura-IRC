package best.azura.irc.client.handler;

import best.azura.irc.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            out.add(PacketUtil.fromJSON(in.toString(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            log.error("Failed to parse Packet!", exception);
        }
    }
}
