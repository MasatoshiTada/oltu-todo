package com.example.authorizationserver.service;

import com.example.authorizationserver.service.user.ResourceOwner;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本来はデータベースからリソースオーナー情報を取得する。
 * （簡略化のためインメモリに保持している）
 */
@ApplicationScoped
public class ResourceOwnerService {

    private static final ConcurrentHashMap<String, ResourceOwner> RESOURCE_OWNER_CACHE = new ConcurrentHashMap<>();

    static {
        RESOURCE_OWNER_CACHE.put("user1", new ResourceOwner("user1", "password"));
        RESOURCE_OWNER_CACHE.put("user2", new ResourceOwner("user2", "password"));
        RESOURCE_OWNER_CACHE.put("user3", new ResourceOwner("user3", "password"));
    }

    public ResourceOwner findByLoginId(String loginId) {
        ResourceOwner source = RESOURCE_OWNER_CACHE.get(loginId);
        return source != null ? new ResourceOwner(source.getLoginId(), source.getPassword()) : null;
    }

}
