package ru.test.alfa.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(
            "Пользователь не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNumberNotFoundException(final PhoneNumberNotFoundException e) {
        return new ErrorResponse(
            "Телефон не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmailNotFoundException(final EmailNotFoundException e) {
        return new ErrorResponse(
            "Email не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientFounds(final InsufficientFounds e) {
        return new ErrorResponse(
            "Недостаточно средств", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValid(final MethodArgumentNotValidException e) {
        return new ErrorResponse(
            "Неверный формат данных", e.getBindingResult().getFieldError().getDefaultMessage()
        );
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateException(final DataIntegrityViolationException e) {
        return new ErrorResponse(
            "Неверный формат данных", e.getMostSpecificCause().getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDeleteLast(final DeleteLastException e) {
        return new ErrorResponse(
            "Неверный запрос", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthException(final SignatureException e) {
        return new ErrorResponse(
            "Пользователь не авторизирован", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyCredentials(final EmptyCredentials e) {
        return new ErrorResponse(
            "Пустой запрос", e.getMessage()
        );
    }
}