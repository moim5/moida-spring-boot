package com.kh.moida.model;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Review {
	 private int reviewId;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 private Date reviewDate;
	 private String reviewTitle;
	 private String reviewContent;
	 private int reviewRate;
	 private Long moimId;
	 private Long fileId;
	 private Long userId;
	 private String fileOrigin;
	 private String fileConvert;
}
