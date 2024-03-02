package ru.test.alfa.exception;

public class InsufficientFounds extends RuntimeException {

    public InsufficientFounds(String e) {
        super(String.format("Баланс: %s", e));
    }
}
