package ru.test.alfa.bankAccount.pojo;

import lombok.Data;

@Data
public class TransferRequest {
    String phoneNumber;
    String email;
    String username;
    double amount;
}
