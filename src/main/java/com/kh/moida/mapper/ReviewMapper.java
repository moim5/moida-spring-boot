package com.kh.moida.mapper;

import java.util.ArrayList;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.model.Review;

@Mapper
public interface ReviewMapper {

	//리뷰리스트뽑기(moimId기준 최신날짜로n개 뽑기)
	public ArrayList<Review> getReviewList(int moimId);

	//후기 등록
	public int writeReview(Review r);
	//후기 등록 (체크)
	public int existWriter(HashMap<String, Object> map);
	
	//후기 뽑기 (edit)
	public Review selectReview(Long reviewId);

	public void updateReviewWithoutFile(Review r);

	public void updateReviewWithFile(Review r);

	//후기삭제
	public int deleteReview(int reviewId);

	//후기 읽기 (read)
	public ArrayList<Review> getReview(Long moimId);

}
