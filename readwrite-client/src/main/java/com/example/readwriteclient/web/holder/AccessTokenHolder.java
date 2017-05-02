package com.example.readwriteclient.web.holder;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * ログイン中のユーザー（リソースオーナー）のアクセストークンを保持する。
 */
@SessionScoped
public class AccessTokenHolder implements Serializable {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 本来はアクセストークンの有効期限のチェックなどが必要だが、
     * 簡略化のためアクセストークンの有無だけでチェックしている。
     */
    public boolean isValidToken() {
        return accessToken != null;
    }

}
