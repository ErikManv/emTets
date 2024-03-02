package ru.test.alfa.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SearchRequest {

    private String fullName;
    private String email;
    private Date birthday;
    private String phoneNumber;
}
