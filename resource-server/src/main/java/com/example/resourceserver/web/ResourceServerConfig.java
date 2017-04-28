package com.example.resourceserver.web;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ResourceServerConfig extends ResourceConfig {

    public ResourceServerConfig() {
        // Moxyを無効化、Jacksonを有効化
        property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, Boolean.TRUE);
        // com.example.web以下の全パッケージをスキャン対象にする
        packages(true, this.getClass().getPackage().getName());
        // @RolesAllowedを利用したフィルタリングを有効化
        register(RolesAllowedDynamicFeature.class);
    }

}
