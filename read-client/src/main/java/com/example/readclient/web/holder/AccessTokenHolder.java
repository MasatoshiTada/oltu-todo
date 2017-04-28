package com.example.readclient.web.holder;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class AccessTokenHolder implements Serializable {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isValidToken() {
        return accessToken != null;
    }

}
