package com.example.readwriteclient.service;

import com.example.readwriteclient.web.constants.Constants;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;
import java.security.GeneralSecurityException;

@ApplicationScoped
public class TokenService {

    @Inject
    SSLContext sslContext;

    public String getAccessToken(String authCode) throws OAuthSystemException, OAuthProblemException {

        // リクエストの生成
        OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                .tokenLocation(Constants.TOKEN_ENDPOINT)
                .setClientId(Constants.CLIENT_ID)
                .setClientSecret(Constants.CLIENT_SECRET)
                .setRedirectURI(Constants.REDIRECT_URI)
                .setCode(authCode)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .buildBodyMessage();
        oAuthClientRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.AUTH_HEADER_VALUE);

        // アクセストークン取得
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthAccessTokenResponse = oAuthClient.accessToken(oAuthClientRequest);
        String accessToken = oAuthAccessTokenResponse.getAccessToken();

        return accessToken;
    }
}
