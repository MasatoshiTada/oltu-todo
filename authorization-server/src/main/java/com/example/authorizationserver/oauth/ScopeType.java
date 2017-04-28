package com.example.authorizationserver.oauth;

/**
 * scopeを表す。
 */
public enum ScopeType {

    READ("read"), WRITE("write");

    private String scopeName;

    ScopeType(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String toString() {
        return scopeName;
    }

}
