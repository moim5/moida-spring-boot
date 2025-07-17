package com.kh.moida.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")  //위치 변경 
    private Date moimDate;
    private int moimMoney;
    private int moimAvgRate;
    private int moimCount;
    private char isVisible;
    private char isActive;
    private int fileId;
    
}
