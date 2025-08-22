package ru.job4j.site.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.job4j.site.exception.IdNotFoundException;

import java.io.IOException;

/**
 * ResponseErrorHandler
 * Кастомная обработка HTTP-ошибок (коды 4xx и 5xx) при работе с RestTemplate.
 * RestTemplate отправляет HTTP-запрос и получает ответ от сервера.
 *
 *     hasError() — это "сторож" или "детектор".
 *     Его единственная задача — посмотреть на статус-код и сказать системе:
 *     "Эй, это ошибка, надо обработать!" (true)
 *     или "Всё в порядке, работай дальше" (false).
 *
 *     handleError() — это "ликвидатор" или "реакция на происшествие".
 *     Его задача — среагировать на факт ошибки, который обнаружил "сторож".
 *     Он получает уже готовый ответ с ошибкой и решает,
 *     что делать дальше — какое исключение бросить, как его сформировать, что залогировать.
 */

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            throw new IdNotFoundException("ID не найден");
        }
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                && httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IdNotFoundException("Пользователь не найден");
        }
    }
}
