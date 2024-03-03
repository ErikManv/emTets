package ru.test.alfa.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ChangeUserInfoRequest {

    String phoneNumber;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    String email;

    String oldPhoneNumber;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    String oldEmail;
}
