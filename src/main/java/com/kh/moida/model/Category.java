package com.kh.moida.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Category {
    private Long categoryId;
    private String categoryName;
    private Long fileId;
    private String fileOrigin;
    private String fileConvert;
}
