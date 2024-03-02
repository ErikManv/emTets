package ru.test.alfa.account.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class TransferRequest {

    String phoneNumber;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    String email;

    String username;

    double amount;
}
