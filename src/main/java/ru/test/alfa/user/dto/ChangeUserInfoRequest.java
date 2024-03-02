package ru.test.alfa.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserInfoRequest {

    @NotBlank(message = "Телефон не может быть пустыми")
    String phoneNumber;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    String email;

    @NotBlank(message = "Телефон не может быть пустыми")
    String oldPhoneNumber;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    String oldEmail;
}
