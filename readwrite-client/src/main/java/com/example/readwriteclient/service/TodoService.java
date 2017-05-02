package com.example.readwriteclient.service;

import com.example.readwriteclient.service.dto.Todo;
import com.example.readwriteclient.web.holder.AccessTokenHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class TodoService {

    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Inject
    AccessTokenHolder accessTokenHolder;
    @Inject
    SSLContext sslContext;

    private static final String TODO_URL = "https://localhost:8070/api/v1/todos";

    @Inject
    ObjectMapper objectMapper;

    /**
     * @return ユーザーの全TODO
     */
    public List<Todo> findAll() throws OAuthSystemException, OAuthProblemException, IOException {
        logger.info("リソースサーバーへリクエスト：GET {}", TODO_URL);

        Response response = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier((s1, s2) -> true)
                .build()
                .target(TODO_URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenHolder.getAccessToken())
                .get();

        logger.info("リソースサーバーからのレスポンス：{}", response.getStatus());

        if (response.getStatusInfo().equals(Response.Status.OK)) {
            String responseBody = response.readEntity(String.class);
            logger.info("リソースサーバーからのレスポンスボディ: {}", responseBody);
            Todo[] todos = objectMapper.readValue(responseBody, Todo[].class);
            List<Todo> todoList = Arrays.asList(todos);
            logger.info("リソースサーバーからTODOが正しく返ってきました : {}", todoList);
            return todoList;
        } else {
            String body = response.readEntity(String.class);
            logger.error("リソースサーバーから200以外のステータスが返ってきました : {}", body);
            throw new RuntimeException("リソースサーバーから200以外のステータスが返ってきました");
        }
    }

    /**
     * TODOを追加する
     * @param todo
     */
    public void add(Todo todo) throws OAuthSystemException, OAuthProblemException, JsonProcessingException {
        logger.info("リソースサーバーへリクエスト：POST {}", TODO_URL);

        String todoJson = objectMapper.writeValueAsString(todo);
        logger.info("JSON変換後の登録対象TODO : {}" + todoJson);

        Response response = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier((s1, s2) -> true)
                .build()
                .target(TODO_URL)
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenHolder.getAccessToken())
                .post(Entity.entity(todoJson, MediaType.APPLICATION_JSON_TYPE));

        logger.info("リソースサーバーからのレスポンス：{}", response.getStatus());

        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            logger.info("リソースサーバーにTODOが正しく登録されました");
        } else {
            String body = response.readEntity(String.class);
            logger.error("リソースサーバーから201以外のステータスが返ってきました : {}", body);
            throw new RuntimeException("リソースサーバーから201以外のステータスが返ってきました");
        }
    }
}
