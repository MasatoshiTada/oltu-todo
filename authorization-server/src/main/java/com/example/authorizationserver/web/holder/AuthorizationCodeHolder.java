package com.example.authorizationserver.web.holder;

import com.example.authorizationserver.oauth.Client;
import com.example.authorizationserver.oauth.ResourceOwner;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class AuthorizationCodeHolder {

    private final ConcurrentHashMap<String, ResourceOwner> resourceOwnerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<>();

    public void addResourceOwner(String authCode, ResourceOwner resourceOwner) {
        resourceOwnerMap.put(authCode, resourceOwner);
    }

    public void addClient(String authCode, Client client) {
        clientMap.put(authCode, client);
    }

    public boolean isValidAuthCode(String authCode) {
        return resourceOwnerMap.containsKey(authCode) && clientMap.containsKey(authCode);
    }

    public void remove(String authCode) {
        resourceOwnerMap.remove(authCode);
        clientMap.remove(authCode);
    }

    public ResourceOwner getResourceOwner(String authCode) {
        return resourceOwnerMap.get(authCode);
    }

    public Client getClient(String authCode) {
        return clientMap.get(authCode);
    }
}
