package com.example.authorizationserver.service;

import com.example.authorizationserver.service.user.ResourceServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotAuthorizedException;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class ResourceServerService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServerService.class);

    private static final CopyOnWriteArrayList<ResourceServer> RESOURCE_SERVERS = new CopyOnWriteArrayList<>();

    static {
        RESOURCE_SERVERS.add(new ResourceServer("resourceserver", "password"));
    }

    public ResourceServer getResourceServer(String resourceServerId, String resourceServerPassword) {
        return RESOURCE_SERVERS.stream()
                .filter(r -> r.getResourceServerId().equals(resourceServerId) && r.getResourceServerSecret().equals(resourceServerPassword))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("リソースサーバーのBASIC認証が失敗しました。IDまたはパスワードが違います");
                    return new NotAuthorizedException("Basic realm=AUTHZ_SERVER");
                });
    }
}
