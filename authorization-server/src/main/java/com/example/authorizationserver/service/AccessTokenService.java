package com.example.authorizationserver.service;

import com.example.authorizationserver.service.user.ResourceOwner;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class AccessTokenService {

    private static final ConcurrentHashMap<String, ResourceOwner> tokenCache = new ConcurrentHashMap<>();

    public void saveAccessTokenAndResourceOwner(String accessToken, ResourceOwner resourceOwner) {
        tokenCache.put(accessToken, resourceOwner);
    }

    public ResourceOwner findResourceOwnerByAccessToken(String accessToken) {
        return tokenCache.get(accessToken);
    }
}
