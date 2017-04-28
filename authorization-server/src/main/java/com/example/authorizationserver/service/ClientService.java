package com.example.authorizationserver.service;

import com.example.authorizationserver.oauth.ScopeType;
import com.example.authorizationserver.service.user.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotAuthorizedException;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private static final CopyOnWriteArrayList<Client> CLIENTS = new CopyOnWriteArrayList<>();

    static {
        CLIENTS.add(new Client("readclient", "password", "https://localhost:8080/api/redirect", ScopeType.READ));
        CLIENTS.add(new Client("readwriteclient", "password", "https://localhost:8081/api/redirect", ScopeType.READ, ScopeType.WRITE));
    }

    public Client getClient(String clientId) {
        return CLIENTS.stream()
                .filter(client -> client.getClientId().equals(clientId))
                .findFirst()
                .get();
    }
}
