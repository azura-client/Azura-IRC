package best.azura.irc.utils;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

public class SSLUtil {

    public static SSLContext createSSLContext(String keystoreContentBase64, String keystorePassword) throws NoSuchAlgorithmException,
            KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        KeyStore keyStore = KeyStore.getInstance("JKS");

        keyStore.load(new ByteArrayInputStream(Base64.getDecoder().decode(keystoreContentBase64)), keystorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        return sslContext;
    }

    public static SSLContext createAllTrustSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);
        return sslContext;
    }

    public static SslContext createAllTrustSSLContextClient() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    }
}
