package com.example.readwriteclient.web.constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Constants {

    private Constants() {}

    public static final String CLIENT_ID = "readwriteclient";
    public static final String CLIENT_SECRET = "password";
    public static final String REDIRECT_URI = "https://readwrite-client-tada.cfapps.io/api/redirect";
    public static final String AUTH_ENDPOINT = "https://auth-server-tada.cfapps.io/api/authorize";
    public static final String TOKEN_ENDPOINT = "https://auth-server-tada.cfapps.io/api/token";

    public static final String AUTH_HEADER_VALUE = "Basic " + Base64.getEncoder()
            .encodeToString(new String(CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8));
}
