package com.example.readwriteclient.web.exception.mapper;

import com.example.readwriteclient.web.constants.Constants;
import com.example.readwriteclient.web.exception.AccessTokenRequiredException;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.*;
import java.net.URI;

/**
 * アクセストークンが無かった場合に発生する例外AccessTokenRequiredExceptionを処理する。
 * 認可サーバーの認可エンドポイントにリダイレクトする。
 */
@Provider
public class AccessTokenRequiredExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<AccessTokenRequiredException> {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenRequiredExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(AccessTokenRequiredException exception) {
        logger.error(exception.getMessage());
        try {
            // 認可サーバーへのリクエストを生成
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation(Constants.AUTH_ENDPOINT)
                    .setResponseType(OAuth.OAUTH_CODE)
                    .setClientId(Constants.CLIENT_ID)
                    .setRedirectURI(Constants.REDIRECT_URI)
                    .setState("xyz")
                    .buildQueryMessage();

            // 認可サーバーの認可エンドポイントへリダイレクト
            String authorizationEndpointUri = oAuthClientRequest.getLocationUri();
            logger.info("認可エンドポイントへリダイレクトします。URL = {}", authorizationEndpointUri);
            return Response.status(Response.Status.FOUND).location(URI.create(authorizationEndpointUri)).build();
        } catch (OAuthSystemException e) {
            logger.error("認可エンドポイントへのアクセス中にエラーが発生しました", e);
            throw new RuntimeException(e);
        }
    }
}
