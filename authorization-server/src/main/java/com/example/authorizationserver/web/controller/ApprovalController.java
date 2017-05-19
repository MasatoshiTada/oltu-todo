package com.example.authorizationserver.web.controller;

import com.example.authorizationserver.service.ClientService;
import com.example.authorizationserver.service.ResourceOwnerService;
import com.example.authorizationserver.oauth.Client;
import com.example.authorizationserver.oauth.ResourceOwner;
import com.example.authorizationserver.web.holder.AuthorizationCodeHolder;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * 認可画面で「認可」ボタンをクリックされた際にアクセスされる。
 * 認可コードを発行して、クライアントのリダイレクトエンドポイントにリダイレクトする。
 */
@Path("/approval")
@ApplicationScoped
public class ApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    @Inject
    ResourceOwnerService resourceOwnerService;
    @Inject
    ClientService clientService;
    @Inject
    AuthorizationCodeHolder authorizationCodeHolder;

    @POST
    public Response approve(@FormParam("loginId") String loginId, @FormParam("password") String password,
                            @FormParam("client_id") String clientId, @FormParam("state") String state,
                            @Context HttpServletRequest httpServletRequest)
            throws OAuthProblemException, OAuthSystemException {
        // リソースオーナーの認証
        ResourceOwner resourceOwner = resourceOwnerService.findByLoginId(loginId);
        if (!resourceOwner.getPassword().equals(password)) {
            logger.error("ログインIDまたはパスワードが違います");
            throw new NotAuthorizedException("Basic realm=AUTHZ_SERVER");
        }
        logger.info("リソースオーナーの認証に成功しました");

        // 認可コードの発行
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        String authCode = oAuthIssuer.authorizationCode();
        logger.info("認可コードを発行しました : {}", authCode);

        // クライアントの取得
        Client client = clientService.getClient(clientId);
        logger.info("クライアント情報 = {}", client);

        // 認可コードとリソースオーナーをホルダーに保存
        authorizationCodeHolder.addResourceOwner(authCode, resourceOwner);
        authorizationCodeHolder.addClient(authCode, client);

        // レスポンスの作成
        OAuthResponse oAuthResponse = OAuthASResponse
                .authorizationResponse(httpServletRequest, Response.Status.FOUND.getStatusCode())
                .setCode(authCode)
                .setParam(OAuth.OAUTH_STATE, state)
                .location(client.getRedirectUri())
                .buildQueryMessage();

        // クライアントのリダイレクトエンドポイントにリダイレクト
        logger.info("クライアントのリダイレクトエンドポイントにリダイレクトします : {}", oAuthResponse.getLocationUri());
        return Response.status(oAuthResponse.getResponseStatus())
                .location(URI.create(oAuthResponse.getLocationUri())).build();
    }
}
