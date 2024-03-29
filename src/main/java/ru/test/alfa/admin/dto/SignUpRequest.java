package ru.test.alfa.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequest {

    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;

    @NotBlank(message = "Телефон не может быть пустыми")
    private String phoneNumber;

    @NotNull(message = "Баланс карты не может быть пустыми")
    private double cardBalance;

    @NotNull(message = "Депозит не может быть пустыми")
    private double initDeposit;

    @NotNull(message = "Дата рождения не может быть пустой")
    private LocalDate dateOfBirth;

    @NotBlank(message = "ФИО не может быть пустыми")
    private String fullName;
}
