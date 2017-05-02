package com.example.authorizationserver.web.controller;

import com.example.authorizationserver.service.ResourceOwnerService;
import com.example.authorizationserver.oauth.Client;
import com.example.authorizationserver.web.filter.ClientAuthenticationRequired;
import com.example.authorizationserver.oauth.ResourceOwner;
import com.example.authorizationserver.service.AccessTokenService;
import com.example.authorizationserver.web.holder.AuthorizationCodeHolder;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/token")
@ApplicationScoped
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Inject
    AccessTokenService accessTokenService;
    @Inject
    AuthorizationCodeHolder authorizationCodeHolder;
    @Inject
    ResourceOwnerService resourceOwnerService;
    @Context
    SecurityContext securityContext;

    @ClientAuthenticationRequired
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(@Context HttpServletRequest httpServletRequest) throws Exception {
        try {
            logger.info("アクセストークン発行開始");
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(httpServletRequest);

            // 認可コードを取得
            String authCode = oauthRequest.getCode();
            logger.info("認可コード = {}", authCode);

            // 認可コードが正当でないならば例外スロー
            if (!authorizationCodeHolder.isValidAuthCode(authCode)) {
                logger.error("認可コードが違います");
                throw OAuthProblemException.error("access_denied");
            }

            // リソースオーナーとクライアントを取得
            ResourceOwner resourceOwner = authorizationCodeHolder.getResourceOwner(authCode);
            Client client = authorizationCodeHolder.getClient(authCode);

            // redirect_uriの正当性をチェック
            validateRedirectionURI(oauthRequest, client);

            // 認可コードを削除
            authorizationCodeHolder.remove(authCode);

            // アクセストークンとリフレッシュトークン発行
            OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            String accessToken = oAuthIssuer.accessToken();
            String refreshToken = oAuthIssuer.refreshToken();

            // アクセストークンとリソースオーナーをホルダーに登録
            logger.info("保存します。アクセストークン = {}、リソースオーナー = {}", accessToken, resourceOwner);
            resourceOwner.setClient(client);
            accessTokenService.saveAccessTokenAndResourceOwner(accessToken, resourceOwner);

            // レスポンスするJSONの作成
            OAuthResponse oAuthResponse = OAuthASResponse
                    .tokenResponse(Response.Status.OK.getStatusCode())
                    .setAccessToken(accessToken)
                    .setExpiresIn("3600")
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();

            logger.info("アクセストークン発行成功。アクセストークン = {}", oAuthResponse.getBody());

            return Response.ok(oAuthResponse.getBody())
                    .build();
        } catch (OAuthProblemException ex) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(Response.Status.UNAUTHORIZED.getStatusCode())
                    .error(ex)
                    .buildJSONMessage();

            logger.error("アクセストークン発行に失敗しました。理由：{}", ex.getError());

            return Response.status(oAuthResponse.getResponseStatus())
                    .entity(oAuthResponse.getBody())
                    .build();
        }
    }

    /**
     * redirect_uriが登録されたものかどうかをチェックする。
     */
    private void validateRedirectionURI(OAuthTokenRequest oAuthRequest, Client client) throws OAuthProblemException {
        String redirectURI = oAuthRequest.getRedirectURI();
        if (!client.getRedirectUri().equals(redirectURI)) {
            logger.error("redirect_uriが違います : {}", redirectURI);
            throw OAuthProblemException.error("unauthorized_client");
        }
    }

}
