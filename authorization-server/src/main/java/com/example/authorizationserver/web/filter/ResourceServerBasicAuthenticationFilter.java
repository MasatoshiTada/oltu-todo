package com.example.authorizationserver.web.filter;

import com.example.authorizationserver.service.ResourceServerService;
import com.example.authorizationserver.oauth.ResourceServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * リソースサーバーのBASIC認証を行う。
 * [at]ResourceServerAuthenticationRequiredが付加されたコントローラーメソッドの直前に実行される。
 */
@ResourceServerAuthenticationRequired
@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class ResourceServerBasicAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServerBasicAuthenticationFilter.class);

    @Inject
    ResourceServerService resourceServerService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String[] values = new String(
                Base64.getDecoder().decode(authorizationHeader.substring("Basic ".length())), StandardCharsets.UTF_8)
                .split(":");
        String resourceServerId = values[0];
        String resourceServerPassword = values[1];

        logger.info("リソースサーバーのBASIC認証を行います : resourceServerId = {}, resourceServerPassword = {}",
                resourceServerId, resourceServerPassword);

        ResourceServer resourceServer = resourceServerService.getResourceServer(resourceServerId, resourceServerPassword);
        if (resourceServer == null) {
            logger.error("リソースサーバーのIDまたはパスワードが正しくありません");
            throw new NotAuthorizedException("Basic realm=AUTHZ_SERVER");
        }
        logger.info("リソースサーバーのBASIC認証が成功しました");
    }
}
