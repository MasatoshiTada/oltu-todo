package com.example.authorizationserver.web.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Provider
public class CharsetFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        MediaType mediaType = (MediaType) headers.getFirst(HttpHeaders.CONTENT_TYPE);
        if (mediaType != null) {
            headers.putSingle(HttpHeaders.CONTENT_TYPE, mediaType.withCharset(StandardCharsets.UTF_8.name()));
        }
    }
}
