package ru.test.alfa.exception;

public class EmailNotFoundException extends RuntimeException {
        public EmailNotFoundException(String e) {
            super(String.format("email %s not found", e));
        }

}
