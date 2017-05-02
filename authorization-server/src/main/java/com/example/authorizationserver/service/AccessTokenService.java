package com.example.authorizationserver.service;

import com.example.authorizationserver.oauth.ResourceOwner;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本来はデータベースへアクセストークンを保存・取得する。
 * （簡略化のためインメモリに保持している）
 */
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
