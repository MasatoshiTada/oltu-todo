package com.example.resourceserver.web.filter;

import com.example.resourceserver.service.user.ResourceOwner;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityContextPersistentFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityContextPersistentFilter.class);

    @Context
    HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        ResourceOwner resourceOwner = (ResourceOwner) httpServletRequest.getAttribute(ResourceOwner.ATTR_NAME);
        String clientId = (String) httpServletRequest.getAttribute(OAuth.OAUTH_CLIENT_ID);

        logger.info("アクセスしたリソースオーナー : {}", resourceOwner);

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return resourceOwner.getLoginId();
                    }
                };
            }

            @Override
            public boolean isUserInRole(String scope) {
                return Arrays.stream(resourceOwner.getClient().getScopeTypes())
                        .anyMatch(scopeType -> scopeType.toString().equals(scope));
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return "BASIC_AUTH";
            }
        });

    }
}
