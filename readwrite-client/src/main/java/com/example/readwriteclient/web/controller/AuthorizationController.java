package com.example.readwriteclient.web.controller;

import com.example.readwriteclient.web.constants.Constants;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/authorization")
@ApplicationScoped
public class AuthorizationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @GET
    public Response authorization(@Context HttpServletRequest httpServletRequest,
                                  @Context HttpServletResponse httpServletResponse) throws OAuthSystemException {
        // 認可サーバーへのリクエストを生成
        OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                .authorizationLocation(Constants.AUTH_ENDPOINT)
                .setResponseType(OAuth.OAUTH_CODE)
                .setClientId(Constants.CLIENT_ID)
                .setRedirectURI(Constants.REDIRECT_URI)
                .setState("xyz")
                .buildQueryMessage();

        // BASIC認証ヘッダー付加（client_id + client_secret）
        oAuthClientRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.AUTH_HEADER_VALUE);

        // 認可サーバーのログイン画面へリダイレクト
        String locationUri = oAuthClientRequest.getLocationUri();
        logger.info("ロケーションURIへリダイレクトします -> {}", locationUri);
        return Response.status(Response.Status.FOUND).location(URI.create(locationUri)).build();
    }
}
