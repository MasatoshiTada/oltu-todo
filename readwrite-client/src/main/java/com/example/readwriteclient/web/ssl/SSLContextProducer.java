package com.example.readwriteclient.web.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@ApplicationScoped
public class SSLContextProducer {

    private static final Logger logger = LoggerFactory.getLogger(SSLContextProducer.class);

    @Produces
    public SSLContext sslContext() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            logger.info("SSLContextの初期化が完了しました");
            return sslContext;
        } catch (GeneralSecurityException e) {
            logger.error("SSLContextの初期化中に例外が発生しました", e);
            throw new RuntimeException(e);
        }

    }
}
