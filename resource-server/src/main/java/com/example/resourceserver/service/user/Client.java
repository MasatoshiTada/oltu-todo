package com.example.resourceserver.service.user;

import com.example.resourceserver.oauth.ScopeType;

import java.util.Arrays;

/**
 * クライアントを表す。
 */
public class Client {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private ScopeType[] scopeTypes;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public ScopeType[] getScopeTypes() {
        return scopeTypes;
    }

    public void setScopeTypes(ScopeType[] scopeTypes) {
        this.scopeTypes = scopeTypes;
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
