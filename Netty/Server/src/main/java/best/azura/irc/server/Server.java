package best.azura.irc.server;

import best.azura.irc.server.chat.InMemoryChat;
import best.azura.irc.server.handler.AuthenticationHandler;
import best.azura.irc.server.handler.PacketDecoder;
import best.azura.irc.server.handler.PacketEncoder;
import best.azura.irc.server.session.InMemoryRepository;
import best.azura.irc.utils.SSLUtil;
import best.azura.irc.utils.executor.Scheduler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.ssl.SslHandler;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Slf4j
public class Server {

    private final int port;
    private final SSLContext sslContext;
    private final SSLEngine sslEngine;
    private ChannelFuture channelFuture;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(4);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    private Controller controller;

    public Server(int port, String base64Cert, String keystorePassword) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        this.port = port;
        sslContext = SSLUtil.createSSLContext(base64Cert, keystorePassword);
        sslEngine = sslContext.createSSLEngine();
        controller = new Controller(new InMemoryChat(20), new Scheduler("serverExecutor", 4), new InMemoryRepository());
    }

    public void start() throws Exception {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast("ssl", new SslHandler(sslEngine))
                                    .addLast("frameDecoder", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()))
                                    .addLast("stringDecoder", new PacketDecoder())
                                    .addLast("stringEncoder", new PacketEncoder())
                                    .addLast("lineEncoder", new LineEncoder(LineSeparator.WINDOWS, StandardCharsets.UTF_8))
                                    .addLast("authHandler", new AuthenticationHandler(controller,false));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = serverBootstrap.bind(port).sync();
            log.info("Server started on port " + port);
        } catch (Exception ignore) {
            Sentry.captureException(ignore);
        }
    }

    public void terminate() {
        try {
            channelFuture.channel().closeFuture().sync();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (Exception ignore) {
            Sentry.captureException(ignore);
        }
    }
}
