package com.example.authorizationserver.web.holder;

import com.example.authorizationserver.service.user.ResourceOwner;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class ResourceOwnerHolder implements Serializable {

    private ResourceOwner resourceOwner;

    public ResourceOwner getResourceOwner() {
        return resourceOwner;
    }

    public void setResourceOwner(ResourceOwner resourceOwner) {
        this.resourceOwner = resourceOwner;
    }
}
