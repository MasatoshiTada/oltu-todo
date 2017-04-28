package com.example.authorizationserver.web.exception.mapper;

import com.example.authorizationserver.web.exception.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<WebApplicationException> {

    private static final Logger logger = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        logger.error("例外が発生しました：", exception);
        return Response.status(exception.getResponse().getStatusInfo())
                .entity(new ErrorDto(exception.getMessage()))
                .build();
    }
}
