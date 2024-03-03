package ru.test.alfa.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;
    private List<String> email;
    private List<String> phoneNumbers;
    private String fullName;
    private LocalDate dateOfBirth;
    private double cardBalance;
    private double deposit;

}
