package ru.test.alfa.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    ANON("ANON");


    private final String vale;

}
