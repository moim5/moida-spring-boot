package com.kh.moida.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Answer {
    private int ansId;
    private String ansContent;
    private Date ansDate;
    private int quesId;
    private int userId;
}
