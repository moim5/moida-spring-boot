package com.kh.moida.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.model.Review;

@Mapper
public interface ReviewMapper {

	//리뷰리스트뽑기(moimId기준 최신날짜로n개 뽑기)
	public ArrayList<Review> getReviewList(int moimId);

	//후기등록
	public int enrollReview(Review r, MultipartFile image);

	//후기 수정
	public Review selectReview(int reviewId);

}
