package com.example.readclient.web.controller;

import com.example.readclient.service.TokenService;
import com.example.readclient.web.holder.AccessTokenHolder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * redirect_uriで指定されるリダイレクトエンドポイント。
 * 認可コードとアクセストークンの引き換えを行った後、一覧画面に遷移する。
 */
@Path("/redirect")
@ApplicationScoped
public class RedirectController {

    @Inject
    AccessTokenHolder accessTokenHolder;
    @Inject
    TokenService tokenService;

    @GET
    @Controller
    public String redirect(@Context HttpServletRequest httpServletRequest)
            throws OAuthProblemException, OAuthSystemException {
        // クエリパラメータから認可コードを取得
        String authCode = httpServletRequest.getParameter(OAuth.OAUTH_CODE);
        // 認可コードと引き換えにアクセストークンを取得
        String accessToken = tokenService.getAccessToken(authCode);
        // セッションスコープなホルダーにアクセストークンを保存
        accessTokenHolder.setAccessToken(accessToken);
        // 一覧画面にリダイレクト
        return "redirect:/todo/index";
    }
}
