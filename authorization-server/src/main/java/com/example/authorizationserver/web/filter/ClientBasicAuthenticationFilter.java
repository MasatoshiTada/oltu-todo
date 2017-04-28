package com.example.authorizationserver.web.filter;

import com.example.authorizationserver.service.ClientService;
import com.example.authorizationserver.web.principal.ClientPrincipal;
import com.example.authorizationserver.service.user.Client;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

/**
 * クライアントのBASIC認証を行う
 */
@ClientAuthenticationRequired
@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class ClientBasicAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ClientBasicAuthenticationFilter.class);

    @Inject
    ClientService clientService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        logger.info("Authorizationヘッダーの値 : {}", authorizationHeader);
        String[] values = new String(
                Base64.getDecoder().decode(authorizationHeader.substring("Basic ".length())), StandardCharsets.UTF_8)
                .split(":");
        String clientId = values[0];
        String clientSecret = values[1];

        logger.info("クライアントのBASIC認証を行います : clientId = {}, clientSecret = {}", clientId, clientSecret);

        Client client = clientService.getClient(clientId);
        logger.info("ClientServiceから取得したclientId = {}, clientSecret = {}", client.getClientId(), client.getClientSecret());
        if (!client.getClientSecret().equals(clientSecret)) {
            throw new NotAuthorizedException("Basic realm=AUTHZ_SERVER");
        }

        logger.info("クライアントのBASIC認証が成功しました");

        ClientPrincipal clientPrincipal = new ClientPrincipal(client);

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return clientPrincipal;
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
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
