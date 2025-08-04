package com.kh.moida.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Question {

    // 질문 관련 필드
    private Integer quesId; //몇번째 문의인지
    private String quesContent;
    private long questionUserId; //작성한 사람
    private Integer moimId;

    // 답변 관련 필드 (JOIN으로 포함됨)
    private Integer ansId; //몇번째 답변인지
    private String ansContent;
    private Date ansDate;
    private Long answerUserId; //작성한 호스트

    private String name; // 유저 이름
}
