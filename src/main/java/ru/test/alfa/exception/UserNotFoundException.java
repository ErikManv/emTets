package ru.test.alfa.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String e) {
        super(String.format("User %s not found", e));
    }
}
