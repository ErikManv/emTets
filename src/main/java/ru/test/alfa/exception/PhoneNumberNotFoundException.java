package ru.test.alfa.exception;

public class PhoneNumberNotFoundException extends RuntimeException {

    public PhoneNumberNotFoundException(String e) {
        super(String.format("номер %s не найден", e));
    }
}
