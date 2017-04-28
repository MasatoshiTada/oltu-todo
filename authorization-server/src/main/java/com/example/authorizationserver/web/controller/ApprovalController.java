package com.example.authorizationserver.web.controller;

import com.example.authorizationserver.service.ClientService;
import com.example.authorizationserver.service.ResourceOwnerService;
import com.example.authorizationserver.service.user.Client;
import com.example.authorizationserver.service.user.ResourceOwner;
import com.example.authorizationserver.web.holder.AuthorizationCodeHolder;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

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
    @Inject
    Models models;

    @POST
    public Response approve(@FormParam("loginId") String loginId, @FormParam("password") String password,
                            @FormParam("client_id") String clientId, @Context HttpServletRequest httpServletRequest)
            throws OAuthProblemException, OAuthSystemException {
        // 認証
        ResourceOwner resourceOwner = resourceOwnerService.findByLoginId(loginId);
        if (!resourceOwner.getPassword().equals(password)) {
            logger.error("ログインIDまたはパスワードが違います");
            throw new NotAuthorizedException("AUTHZ_SERVER");
        }
        logger.info("認証に成功しました");

        // 認可コード発行
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        String authCode = oAuthIssuer.authorizationCode();
        logger.info("認可コードを発行しました : {}", authCode);

        // クライアントの取得
        Client client = clientService.getClient(clientId);
        logger.info("クライアント情報 = {}", client);

        // 認可コードとリソースオーナーをホルダーに登録
        authorizationCodeHolder.addResourceOwner(authCode, resourceOwner);
        authorizationCodeHolder.addClient(authCode, client);

        // レスポンスの作成
        OAuthResponse oAuthResponse = OAuthASResponse
                .authorizationResponse(httpServletRequest, Response.Status.FOUND.getStatusCode())
                .setCode(authCode)
                .location(client.getRedirectUri())
                .buildQueryMessage();

        // redirect_uriにリダイレクト
        logger.info("redirect_uriにリダイレクトします : {}", oAuthResponse.getLocationUri());
        return Response.status(oAuthResponse.getResponseStatus())
                .location(URI.create(oAuthResponse.getLocationUri())).build();
    }
}
