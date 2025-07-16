package com.kh.moida.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Moim {
    private int moimId;
    private String moimName;
    private String moimType; //ERD : char?
    private String moimContent;
    private String moimAttendeeCount;
    private String moimZipCode;
    private String moimAddress1;
    private String moimAddress2;
    private Date moimDate;
    private int moimMoney;
    private int moimAvgRate;
    private int moimCount;
    private char isVisible;
    private char isActive;
    private int fileId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    
    
}
