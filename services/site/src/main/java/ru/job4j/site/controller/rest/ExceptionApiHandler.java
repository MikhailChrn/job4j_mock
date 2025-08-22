package ru.job4j.site.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.job4j.site.domain.ErrorMessage;
import ru.job4j.site.exception.IdNotFoundException;

/**
 * @RestControllerAdvice
 * Глобальный перехватчик исключений для всех контроллеров приложения.
 * Позволяет в одном централизованном классе обрабатывать исключения,
 * выброшенные в любом контроллере.
 * Это избавляет от необходимости дублировать @ExceptionHandler в каждом контроллере.
 *
 * @ExceptionHandler
 * Локальный "перехватчик" исключений внутри одного контроллера.
 * Позволяет обработать конкретное исключение, выброшенное в методах этого же контроллера,
 * и вернуть клиенту адекватный, красивый ответ (например, JSON с описанием ошибки и HTTP-статусом),
 * вместо стандартной страницы с ошибкой.
 *
 * Они идеально работают в паре: вы создаете глобальный класс (@RestControllerAdvice)
 * и наполняете его методами-обработчиками (@ExceptionHandler).
 *
 * Принцип работы данного класса:
 * 1.Исключение "всплывает" из глубины RestTemplate наружу,
 * в тот метод вашего сервиса/контроллера, откуда был вызван restTemplate.
 * 2. ExceptionApiHandler (глобальный перехватчик) ловит это "всплывшее" исключение IdNotFoundException.
 * 3. Перехватчик формирует красивый ответ: Статус 404 NOT_FOUND и тело в формате JSON с сообщением об ошибке.
 */

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(IdNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
