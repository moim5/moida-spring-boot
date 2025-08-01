package com.kh.moida.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MoimAttendeeWithUser {
    private Long userId;
    private Long moimId;
    private Date attDate;

    private String username;
    private String name;
    private String phone;
    private String email;
}
