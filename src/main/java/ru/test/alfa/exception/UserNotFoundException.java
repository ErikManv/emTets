package ru.test.alfa.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String e) {
        super(String.format("пользователь %s не найден", e));
    }
}
