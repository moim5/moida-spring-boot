package com.kh.moida.notice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Question {

    private Integer  quesId;
    private String quesContent;
    private long  userId;
    private Integer  moimId;
}
