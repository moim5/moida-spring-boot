package com.kh.moida.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MoimAttendee {
    private Long userId;
    private Long moimId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attDate;
}
