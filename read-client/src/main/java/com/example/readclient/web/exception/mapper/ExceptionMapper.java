package com.example.readclient.web.exception.mapper;

import com.example.readclient.web.exception.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
@ApplicationScoped
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Inject
    Models models;

    @Override
    public Response toResponse(Exception exception) {
        URI uri = uriInfo.getAbsolutePath();
        logger.error("{}へのリクエストで例外が発生しました", uri);
        logger.error("スタックトレース：", exception);
        models.put("errorDto", new ErrorDto(exception.getMessage()));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("error.html")
                .build();
    }
}
