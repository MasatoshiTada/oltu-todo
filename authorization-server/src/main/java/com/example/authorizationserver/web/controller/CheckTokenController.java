package com.example.authorizationserver.web.controller;


import com.example.authorizationserver.web.filter.ResourceServerAuthenticationRequired;
import com.example.authorizationserver.oauth.ResourceOwner;
import com.example.authorizationserver.web.exception.dto.ErrorDto;
import com.example.authorizationserver.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * リソースサーバーから、アクセストークンの正当性をチェックするためにアクセスされる。
 * アクセストークンが正当な場合、リソースオーナー情報・スコープ情報などをJSONでレスポンスする。
 */
@Path("/check_token")
@ApplicationScoped
public class CheckTokenController {

    private static final Logger logger = LoggerFactory.getLogger(CheckTokenController.class);

    @Inject
    AccessTokenService accessTokenService;

    @ResourceServerAuthenticationRequired
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkToken(@FormParam("access_token") String accessToken) {
        // アクセストークンからリソースオーナー情報を取得する
        ResourceOwner resourceOwner = accessTokenService.findResourceOwnerByAccessToken(accessToken);
        logger.info("リソースオーナー情報 {}", resourceOwner);
        if (resourceOwner != null) {
            logger.info("アクセストークンは正当です : {}", accessToken);
            return Response.ok(resourceOwner).build();
        } else {
            logger.error("アクセストークンが正当ではありません : {}", accessToken);
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto("アクセストークンが正当ではありません")).build();
        }
    }
}
