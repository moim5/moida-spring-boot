package com.kh.moida.dto;

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
public class ReviewWithUser {
	 private Long reviewId;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 private Date reviewDate;
	 private String reviewTitle;
	 private String reviewContent;
	 private int reviewRate;
	 
	 private String username;
	 private Long userId;
	 
	 private Long moimId;
	 
	 private String fileConvert;
	 private String fileOrigin;

	 
	 
}
