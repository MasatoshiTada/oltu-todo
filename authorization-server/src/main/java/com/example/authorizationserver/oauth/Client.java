package com.example.authorizationserver.oauth;

import com.example.authorizationserver.oauth.ScopeType;

import java.util.Arrays;

/**
 * クライアントを表す。
 */
public class Client {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private ScopeType[] scopeTypes;

    public Client(String clientId, String clientSecret, String redirectUri, ScopeType... scopeTypes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopeTypes = scopeTypes;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public ScopeType[] getScopeTypes() {
        return scopeTypes;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                ", scopeTypes=" + Arrays.toString(scopeTypes) +
                '}';
    }
}
