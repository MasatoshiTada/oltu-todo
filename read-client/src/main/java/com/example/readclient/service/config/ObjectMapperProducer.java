package com.example.readclient.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.TimeZone;

@ApplicationScoped
public class ObjectMapperProducer {

    @Produces
    public ObjectMapper objectMapper() {
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
