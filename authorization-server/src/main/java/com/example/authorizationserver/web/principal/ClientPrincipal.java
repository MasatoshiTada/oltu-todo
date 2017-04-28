package com.example.authorizationserver.web.principal;

import com.example.authorizationserver.service.user.Client;

import java.security.Principal;

public class ClientPrincipal implements Principal {

    private Client client;

    public ClientPrincipal(Client client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return client.getClientId();
    }

    public String getRedirectUri() {
        return client.getRedirectUri();
    }

    public Client getClient() {
        return client;
    }
}
