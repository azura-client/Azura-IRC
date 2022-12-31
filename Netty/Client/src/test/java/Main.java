import best.azura.irc.client.Client;
import best.azura.irc.client.packets.client.ChatMessagePacket;
import best.azura.irc.client.packets.client.UserAuthenticationPacket;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Main {

    static Client client;

    public static void main(String[] args) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
        byte[] keystoreFile = Files.readAllBytes(new File("config", "irc.keystore").toPath());
        client = new Client("localhost", 6968, Base64.encodeBase64String(keystoreFile), args[0]);
        client.connect();
    }

}
