package ru.test.alfa.exception;

public class EmptyCredentials extends RuntimeException{

    public EmptyCredentials() {
        super("Пустой запрос");
    }
}
