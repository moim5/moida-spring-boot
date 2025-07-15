package com.kh.moida.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Long userId;
    private String username;
    private String password;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String phone;
    private String email;
    private String zipCode;
    private String address1;
    private String address2;
    private String gender;
    private String isActive;
    private String isAdmin;
}
