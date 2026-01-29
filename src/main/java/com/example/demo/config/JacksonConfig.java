package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("d/M/yyyy"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss"));
        registrar.registerFormatters(registry);
    }
}
