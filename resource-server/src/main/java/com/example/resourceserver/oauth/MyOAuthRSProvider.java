package com.example.resourceserver.oauth;

import com.example.resourceserver.service.user.ResourceOwner;
import com.example.resourceserver.web.constants.Constants;
import com.example.resourceserver.web.ssl.SSLContextUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.rsfilter.OAuthClient;
import org.apache.oltu.oauth2.rsfilter.OAuthDecision;
import org.apache.oltu.oauth2.rsfilter.OAuthRSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.TimeZone;

/**
 * OAuthFilter内で呼び出される処理。
 * 認可サーバーへのアクセスなどを行って、アクセストークンとscopeの正当性チェックを行う。
 * web.xmlのoauth.rs.provider-classコンテキストパラメータの値に、
 * このクラスのFQCNを指定する必要がある。
 * @see org.apache.oltu.oauth2.rsfilter.OAuthFilter
 */
public class MyOAuthRSProvider implements OAuthRSProvider {

    private static final Logger logger = LoggerFactory.getLogger(MyOAuthRSProvider.class);

    private final ObjectMapper objectMapper;

    public MyOAuthRSProvider() {
        objectMapper = new ObjectMapper();
        // Pretty Printを有効化
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Date and Time APIを解釈するためのモジュール追加
        objectMapper.registerModule(new JavaTimeModule());
        // 日付のフォーマットをyyyy-MM-dd形式に指定
        objectMapper.setDateFormat(new StdDateFormat());
        // タイムゾーンを日本時間に指定
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        // JavaのキャメルケースとJSONのスネークケースを自動変換
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     *
     * @param rsId web.xmlに設定したoauth.rs.realmコンテキストパラメータの値
     * @param accessToken アクセストークン
     * @param httpServletRequest HttpServletRequest
     * @return
     * @throws OAuthProblemException
     */
    @Override
    public OAuthDecision validateRequest(String rsId, String accessToken, HttpServletRequest httpServletRequest) throws OAuthProblemException {

        // 認可サーバーへHTTPアクセスしてアクセストークンの正当性チェックを行う
        MultivaluedHashMap<String, String> formParams = new MultivaluedHashMap<>();
        formParams.putSingle("access_token", accessToken);
        Response response = ClientBuilder.newBuilder()
                .sslContext(SSLContextUtil.getSslContext()) // 自己証明書対策（本番では使わないでください）
                .hostnameVerifier((s1, s2) -> true) // 自己証明書対策（本番では使わないでください）
                .build()
                .target(Constants.CHECK_TOKEN_URI)
                .request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, Constants.AUTH_HEADER_VALUE)
                .post(Entity.form(formParams));

        logger.info("認可サーバーからのレスポンスコード : {}", response.getStatusInfo());

        // アクセストークンは正当ならば200が返ってくる
        if (response.getStatusInfo().equals(Response.Status.OK)) {
            // レスポンスのJSONを受け取る
            String responseJson = response.readEntity(String.class);
            ResourceOwner resourceOwner = readValue(responseJson);
            logger.info("認可サーバーから取得したリソースオーナー情報 : {}", resourceOwner);

            // リソースオーナー情報をSecurityContextFilterに渡す
            httpServletRequest.setAttribute(ResourceOwner.ATTR_NAME, resourceOwner);

            logger.info("アクセストークンは正当です : {}", accessToken);

            return new OAuthDecision() {
                @Override
                public Principal getPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return resourceOwner.getLoginId();
                        }
                    };
                }

                @Override
                public OAuthClient getOAuthClient() {
                    return new OAuthClient() {
                        @Override
                        public String getClientId() {
                            return resourceOwner.getClient().getClientId();
                        }
                    };
                }
            };
        } else {
            // アクセストークンが正当でない場合（認可サーバーから200以外のレスポンスが返ってきた場合）
            logger.error("アクセストークンが正当ではありません : {}", accessToken);
            throw OAuthProblemException.error("access_denied");
        }
    }

    private ResourceOwner readValue(String json) throws OAuthProblemException {
        try {
            return objectMapper.readValue(json, ResourceOwner.class);
        } catch (IOException e) {
            logger.error("リソースオーナー情報のJSON解析中にエラーが発生しました。JSONの内容 : {}", json);
            logger.error("スタックトレース", e);
            throw OAuthProblemException.error("server_error", "json_error");
        }
    }
}
