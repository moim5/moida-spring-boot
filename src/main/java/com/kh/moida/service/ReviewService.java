package com.kh.moida.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kh.moida.mapper.ReviewMapper;
import com.kh.moida.model.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewMapper mapper;

	
	//review객체배열 
	public ArrayList<Review> getReviewList() {
		return mapper.getReviewList();
	}

	//리뷰 이미 작성했을 경우 write view이동 막기
	public int countReview(int reviewId) {
		return mapper.countReview(reviewId);
	}

}
