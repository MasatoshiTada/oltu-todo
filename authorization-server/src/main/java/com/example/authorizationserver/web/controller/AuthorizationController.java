package com.example.authorizationserver.web.controller;

import com.example.authorizationserver.service.ClientService;
import com.example.authorizationserver.service.user.Client;
import com.example.authorizationserver.web.holder.AuthorizationCodeHolder;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/authorize")
@ApplicationScoped
public class AuthorizationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @Inject
    AuthorizationCodeHolder authorizationCodeHolder;
    @Inject
    ClientService clientService;
    @Inject
    Models models;

    @GET
    @Controller
    public String goToApprovalView(@Context HttpServletRequest httpServletRequest) throws OAuthProblemException, OAuthSystemException {
        // リクエストの生成（必要なパラメータのチェックも行う）
        OAuthAuthzRequest oAuthRequest = new OAuthAuthzRequest(httpServletRequest);
        // クライアントを取得
        String clientId = oAuthRequest.getClientId();
        Client client = clientService.getClient(clientId);
        // redirect_uriの正当性をチェック
        validateRedirectionURI(oAuthRequest, client);
        // approval画面へフォワード
        models.put("client", client);
        logger.info("approval画面に遷移します");
        return "approval.html";
    }

    /**
     * redirect_uriが登録されたものかどうかをチェックする。
     */
    private void validateRedirectionURI(OAuthAuthzRequest oAuthRequest, Client client) throws OAuthProblemException {
        String redirectURI = oAuthRequest.getRedirectURI();
        if (!client.getRedirectUri().equals(redirectURI)) {
            logger.error("redirect_uriが違います : {}", redirectURI);
            throw OAuthProblemException.error("invalid_request");
        }
    }
}
