package com.kh.moida.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class File {
    private Long fileId;
    private String fileOrigin;
    private String fileConvert;
}
