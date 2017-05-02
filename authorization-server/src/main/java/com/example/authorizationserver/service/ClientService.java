package com.example.authorizationserver.service;

import com.example.authorizationserver.oauth.ScopeType;
import com.example.authorizationserver.oauth.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 本来はデータベースからクライアント情報を取得する。
 * （簡略化のためインメモリに保持している）
 */
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
