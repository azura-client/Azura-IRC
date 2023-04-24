package best.azura.irc.client;

import best.azura.irc.client.handler.ClientHandler;
import best.azura.irc.client.handler.PacketDecoder;
import best.azura.irc.client.handler.PacketEncoder;
import best.azura.irc.client.packets.base.IPacket;
import best.azura.irc.utils.SSLUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Slf4j
public class Client {

    private String host;
    private int port;

    private Channel channel;

    private final SSLContext sslContext;

    private volatile SSLEngine sslEngine;

    private Client instance;

    private final EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    public Client(String host, int port, String base64Keystore, String keystorePassword) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        instance = this;
        this.host = host;
        this.port = port;
        sslContext = SSLUtil.createSSLContext(base64Keystore, keystorePassword);
        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
    }

    public void connect() {
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    ////.addLast("ssl", new SslHandler(sslEngine))
                                    .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(5120, 0, 4, 0, 4))
                                    .addLast( "frameEncoder", new LengthFieldPrepender(4, false))
                                    .addLast("stringDecoder", new PacketDecoder())
                                    .addLast("stringEncoder", new PacketEncoder())
                                    .addLast("handler", new ClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            channel = bootstrap.connect(host, port).sync().channel();
            if (channel.isActive()) {
                log.info("Connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public void send(IPacket packet) {
        ChannelFuture channelFuture = channel.writeAndFlush(packet);
        try {
            log.info("Got Channel Id " + channelFuture.sync().channel().id());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
