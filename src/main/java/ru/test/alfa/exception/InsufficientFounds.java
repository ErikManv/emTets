package ru.test.alfa.exception;

public class InsufficientFounds extends RuntimeException {

    public InsufficientFounds(String e) {
        super(String.format("Insufficient founds. Balance: %s", e));
    }
}
