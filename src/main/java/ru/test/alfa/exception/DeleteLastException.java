package ru.test.alfa.exception;

public class DeleteLastException extends RuntimeException {
        public DeleteLastException(String e) {
            super(String.format("Нельзя удалить последний %s", e));
        }

}
