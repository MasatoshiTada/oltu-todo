package com.example.readwriteclient.web.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.TimeZone;

/**
 * JacksonのObjectMapperに関する設定を記述する。
 */
@Provider
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper objectMapper = new ObjectMapper();
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
        return objectMapper;
    }
}
