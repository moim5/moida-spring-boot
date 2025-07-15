package com.kh.moida.model;

import lombok.*;

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
    private Date birthday;
    private String phone;
    private String email;
    private String zipCode;
    private String address1;
    private String address2;
    private char gender;
    private char isActive;
    private char isAdmin;
}
