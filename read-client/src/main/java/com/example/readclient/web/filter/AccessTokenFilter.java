package com.example.readclient.web.filter;

import com.example.readclient.web.exception.AccessTokenRequiredException;
import com.example.readclient.web.holder.AccessTokenHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@AccessTokenRequired
@Provider
@ApplicationScoped
public class AccessTokenFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    @Inject
    AccessTokenHolder accessTokenHolder;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // アクセストークンを持ってなかったらリダイレクト
        if (!accessTokenHolder.isValidToken()) {
            logger.error("アクセストークンがありません");
            throw new AccessTokenRequiredException("アクセストークンがありません");
        }
        logger.info("アクセストークンがありました");
    }
}
