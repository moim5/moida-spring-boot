package com.kh.moida.notice;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Notice {
    private int noticeId;
    private String noticeTitle;
    private String noticeContent;
    private Date noticeDate;
    private int noticeCount;
    private int fileId;

}
