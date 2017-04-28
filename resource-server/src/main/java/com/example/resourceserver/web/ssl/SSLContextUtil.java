package com.example.resourceserver.web.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 自己証明書対策
 * （本番環境では真似しないでください）
 */
public final class SSLContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(SSLContextUtil.class);

    private static final SSLContext SSL_CONTEXT;

    private SSLContextUtil() {}

    static {
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
            SSL_CONTEXT = SSLContext.getInstance("SSL");
            SSL_CONTEXT.init(null, trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(SSL_CONTEXT.getSocketFactory());
            logger.info("SSLContextの初期化が完了しました");
        } catch (GeneralSecurityException e) {
            logger.error("SSLContext初期化中に例外が発生しました", e);
            throw new RuntimeException(e);
        }
    }

    public static SSLContext getSslContext() {
        return SSL_CONTEXT;
    }
}
