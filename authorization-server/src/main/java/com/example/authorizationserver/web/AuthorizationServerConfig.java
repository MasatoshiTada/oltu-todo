package com.example.authorizationserver.web;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RSを有効化する設定クラス。
 */
@ApplicationPath("/api")
public class AuthorizationServerConfig extends ResourceConfig {

    public AuthorizationServerConfig() {
        // Moxyを無効化してJacksonを有効化
        property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, Boolean.TRUE);
        packages(true, this.getClass().getPackage().getName());
    }
}
