package com.example.readwriteclient.web.converter;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateParamConverter implements ParamConverter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate fromString(String value) {
        return LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String toString(LocalDate value) {
        return value.format(FORMATTER);
    }
}
