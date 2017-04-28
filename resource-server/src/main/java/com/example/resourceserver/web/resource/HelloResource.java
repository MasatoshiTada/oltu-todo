package com.example.resourceserver.web.resource;

import com.example.resourceserver.oauth.ScopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloResource {

    private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);

    @RolesAllowed(ScopeType.READ_STRING)
    @GET
    public Response hello() {
        logger.info("200 ");
        return Response.ok("hello!!!").build();
    }
}
