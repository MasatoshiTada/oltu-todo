package com.example.readwriteclient.web.controller;

import com.example.readwriteclient.web.constants.Constants;
import com.example.readwriteclient.web.holder.AccessTokenHolder;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.annotation.Controller;
import javax.net.ssl.SSLContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;

/**
 * redirect_uriで指定されるリダイレクトエンドポイント。
 * 認可コードとアクセストークンの引き換えを行った後、一覧画面にリダイレクトする。
 */
@Path("/redirect")
@ApplicationScoped
public class RedirectController {

    @Inject
    AccessTokenHolder accessTokenHolder;

    @Inject
    SSLContext sslContext;

    @GET
    @Controller
    public String redirect(@QueryParam(OAuth.OAUTH_CODE) String authCode)
            throws OAuthProblemException, OAuthSystemException {
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

        // トークンエンドポイントにリクエストして、アクセストークンを取得
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthAccessTokenResponse = oAuthClient.accessToken(oAuthClientRequest);
        String accessToken = oAuthAccessTokenResponse.getAccessToken();

        // セッションスコープなホルダーにアクセストークンを保存
        accessTokenHolder.setAccessToken(accessToken);

        // 一覧画面にリダイレクト
        return "redirect:/todo/index";
    }
}
