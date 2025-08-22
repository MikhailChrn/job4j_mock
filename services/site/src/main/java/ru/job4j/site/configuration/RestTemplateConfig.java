package ru.job4j.site.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.job4j.site.handler.RestTemplateResponseErrorHandler;

/**
 * Тут переопределяем стандартное поведение 'RestTemplate' при получении HTTP-ошибок,
 * чтобы заставить его использовать нашу кастомную логику оповещения вместо встроенной.
 *
 * Без нашего обработчика: Spring использует дефолтный DefaultResponseErrorHandler.
 *
 *     DefaultResponseErrorHandler — это инструмент фреймворка.
 *     RestTemplateResponseErrorHandler — это инструмент бизнес-логики.
 */

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
