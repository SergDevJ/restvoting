package ru.ssk.restvoting.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source);
    }
}
