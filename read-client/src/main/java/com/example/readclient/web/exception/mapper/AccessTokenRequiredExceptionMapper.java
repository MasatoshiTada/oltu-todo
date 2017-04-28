package com.example.readclient.web.exception.mapper;

import com.example.readclient.web.controller.AuthorizationController;
import com.example.readclient.web.exception.AccessTokenRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.*;
import java.net.URI;

/**
 * アクセストークンがなかったら、トークン取得のためのURLにリダイレクトする。
 */
@Provider
public class AccessTokenRequiredExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<AccessTokenRequiredException> {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenRequiredExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(AccessTokenRequiredException exception) {
        logger.error(exception.getMessage());
        URI location = uriInfo.getBaseUriBuilder()
                .path(AuthorizationController.class)
                .build();
        logger.info("{}にリダイレクトします", location);
        return Response.status(Response.Status.FOUND).location(location).build();
    }
}
