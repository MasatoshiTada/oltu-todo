package com.example.authorizationserver.web.controller;

import com.example.authorizationserver.service.ResourceOwnerService;
import com.example.authorizationserver.service.user.Client;
import com.example.authorizationserver.web.filter.ClientAuthenticationRequired;
import com.example.authorizationserver.web.principal.ClientPrincipal;
import com.example.authorizationserver.service.user.ResourceOwner;
import com.example.authorizationserver.service.AccessTokenService;
import com.example.authorizationserver.web.holder.AuthorizationCodeHolder;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
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
            OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());

            // 認可コードの正当性のチェック
            String authCode = oauthRequest.getCode();
            logger.info("認可コード = {}", authCode);

            // 認可コードが正当でないならば例外スロー
            if (!authorizationCodeHolder.isValidAuthCode(authCode)) {
                logger.error("認可コードが違います");
                throw OAuthProblemException.error("access_denied");
            }

            // 認可コードが正当ならばリソースオーナーとクライアントを取得し、認可コードを削除
            ResourceOwner resourceOwner = authorizationCodeHolder.getResourceOwner(authCode);
            Client client = authorizationCodeHolder.getClient(authCode);
            resourceOwner.setClient(client);
            authorizationCodeHolder.remove(authCode);

            // アクセストークンとリフレッシュトークン発行
            String accessToken = oAuthIssuer.accessToken();
            String refreshToken = oAuthIssuer.refreshToken();

            // アクセストークンとリソースオーナーをホルダーに登録
            logger.info("保存します。アクセストークン = {}、リソースオーナー = {}", accessToken, resourceOwner);
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
    private void validateRedirectionURI(OAuthAuthzRequest oAuthRequest) throws OAuthProblemException {
        String redirectURI = oAuthRequest.getRedirectURI();
        ClientPrincipal clientPrincipal = (ClientPrincipal) securityContext.getUserPrincipal();
        if (!clientPrincipal.getRedirectUri().equals(redirectURI)) {
            logger.error("redirect_uriが違います : {}", redirectURI);
            throw OAuthProblemException.error("unauthorized_client");
        }
    }

}
