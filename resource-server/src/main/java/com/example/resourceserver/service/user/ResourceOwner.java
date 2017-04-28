package com.example.resourceserver.service.user;

public class ResourceOwner {

    public static final String ATTR_NAME = "ResourceOwner";

    private String loginId;

    private Client client;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "ResourceOwner{" +
                "loginId='" + loginId + '\'' +
                ", client=" + client +
                '}';
    }
}
