package com.example.authorizationserver.service.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResourceOwner {

    private String loginId;

    @JsonIgnore
    private String password;

    private Client client;

    public ResourceOwner(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                ", password='" + password + '\'' +
                ", client=" + client +
                '}';
    }
}
