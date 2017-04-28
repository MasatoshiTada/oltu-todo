package com.example.authorizationserver.web;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class AuthorizationServerConfig extends ResourceConfig {

    public AuthorizationServerConfig() {
        property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, Boolean.TRUE);
        packages(true, this.getClass().getPackage().getName());
    }
}
