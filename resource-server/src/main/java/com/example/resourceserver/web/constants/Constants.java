package com.example.resourceserver.web.constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Constants {

    private static final String RESOURCE_SERVER_ID = "resourceserver";
    private static final String RESOURCE_SERVER_PASSWORD = "password";

    public static final String AUTH_HEADER_VALUE =
            "Basic " + Base64.getEncoder().encodeToString(
                    (RESOURCE_SERVER_ID + ":" + RESOURCE_SERVER_PASSWORD).getBytes(StandardCharsets.UTF_8));

    public static final String CHECK_TOKEN_URI = "https://auth-server-tada.cfapps.io/api/check_token";


}
