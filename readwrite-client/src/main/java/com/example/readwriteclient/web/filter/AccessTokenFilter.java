package com.example.readwriteclient.web.filter;

import com.example.readwriteclient.web.exception.AccessTokenRequiredException;
import com.example.readwriteclient.web.holder.AccessTokenHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * [at]AccessTokenRequiredアノテーションが付加されたコントローラーメソッドの直前に実行される。
 * アクセストークンの有無を判定し、無ければ例外を発生させる。
 * @see AccessTokenRequired
 * @see AccessTokenRequiredException
 * @see com.example.readwriteclient.web.exception.mapper.AccessTokenRequiredExceptionMapper
 */
@AccessTokenRequired
@Provider
@ApplicationScoped
public class AccessTokenFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    @Inject
    AccessTokenHolder accessTokenHolder;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // アクセストークンを持ってなかったら例外発生
        if (!accessTokenHolder.isValidToken()) {
            logger.error("アクセストークンがありません");
            throw new AccessTokenRequiredException("アクセストークンがありません");
        }
        logger.info("アクセストークンがありました");
    }
}
