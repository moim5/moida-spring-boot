package com.kh.moida.mapper;

import java.util.ArrayList;

import java.util.HashMap;

import com.kh.moida.dto.ReviewWithUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.moida.model.Review;

@Mapper
public interface ReviewMapper {

	//리뷰리스트뽑기(moimId기준 최신날짜로n개 뽑기)
	ArrayList<ReviewWithUser> getReviewList(Long moimId);

	//후기 등록
	int writeReview(Review r);
	//후기 등록 (체크)
	int existWriter(HashMap<String, Object> map);
	
	//후기 뽑기 (edit)
	Review selectReview(Long reviewId);

	void updateReviewWithoutFile(Review r);

	void updateReviewWithFile(Review r);

	//후기삭제
	int deleteReview(int reviewId);

	//평균 별점 구하기
	 Double getRateAvgByMoimId(@Param("moimId") Long moimId);

	 //review_read전용 리스트 뽑기
	ArrayList<ReviewWithUser> getReview(Long moimId);
}
