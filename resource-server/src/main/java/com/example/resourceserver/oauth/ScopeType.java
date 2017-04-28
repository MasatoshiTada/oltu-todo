package com.example.resourceserver.oauth;

/**
 * scopeを表す。
 * [at]RolesAllowedで指定するための定数クラス。
 */
public enum ScopeType {

    READ("read"), WRITE("write");

    private String scopeName;

    public static final String READ_STRING = "read";
    public static final String WRITE_STRING = "write";

    ScopeType(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String toString() {
        return scopeName;
    }
}
