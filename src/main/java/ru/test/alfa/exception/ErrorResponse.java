package ru.test.alfa.exception;

public class ErrorResponse {

    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
