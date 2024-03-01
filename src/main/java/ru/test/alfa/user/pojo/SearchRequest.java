package ru.test.alfa.user.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchRequest {

    // like
    private String fullName;
    // full equal
    private String email;
    //bigger then this
    private Date birthday;
    // full equal
    private String phoneNumber;

}
