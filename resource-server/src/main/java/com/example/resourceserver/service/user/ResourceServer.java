package com.example.resourceserver.service.user;

/**
 * リソースサーバーを表す。
 */
public class ResourceServer {
    private String resourceServerId;
    private String resourceServerSecret;

    public ResourceServer(String resourceServerId, String resourceServerSecret) {
        this.resourceServerId = resourceServerId;
        this.resourceServerSecret = resourceServerSecret;
    }

    public String getResourceServerId() {
        return resourceServerId;
    }

    public String getResourceServerSecret() {
        return resourceServerSecret;
    }

    @Override
    public String toString() {
        return "ResourceServer{" +
                "resourceServerId='" + resourceServerId + '\'' +
                ", resourceServerSecret='" + resourceServerSecret + '\'' +
                '}';
    }
}
