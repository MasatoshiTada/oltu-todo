package com.example.readclient.web.constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Constants {

    private Constants() {}

    public static final String CLIENT_ID = "readclient";
    public static final String CLIENT_SECRET = "password";
    public static final String REDIRECT_URI = "https://localhost:8080/api/redirect";
    public static final String AUTH_ENDPOINT = "https://localhost:8888/api/authorize";
    public static final String TOKEN_ENDPOINT = "https://localhost:8888/api/token";

    public static final String AUTH_HEADER_VALUE = "Basic " + Base64.getEncoder()
            .encodeToString(new String(CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8));
}
