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
    private Long moimId;
    private String moimTitle;
    private String moimType;
    private String moimContent;
    private String moimAttendeeCount;
    private String moimZipCode;
    private String moimAddress1;
    private String moimAddress2;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date moimDate;
    private int moimMoney;
    private int moimAvgRate;
    private int moimCount;
    private String isVisible;
    private String isActive;
    private String moimHostIntro;
    private Long userId;
    private Long categoryId;
    private Long fileId;
    private String fileOrigin;
    private String fileConvert;
}
