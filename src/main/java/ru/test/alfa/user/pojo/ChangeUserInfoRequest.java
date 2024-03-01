package ru.test.alfa.user.pojo;

import lombok.Data;

@Data
public class ChangeUserInfoRequest {

    String phoneNumber;
    String email;
    String oldPhoneNumber;
    String oldEmail;
}
