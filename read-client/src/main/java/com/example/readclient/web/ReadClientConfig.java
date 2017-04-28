package com.example.readclient.web;

import org.glassfish.jersey.CommonProperties;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

@ApplicationPath("/api")
public class ReadClientConfig extends Application {

    @Override
    public Map<String, Object> getProperties() {
        HashMap<String, Object> prop = new HashMap<>();
        prop.put(CommonProperties.MOXY_JSON_FEATURE_DISABLE_CLIENT, Boolean.TRUE);
        return prop;
    }
}
