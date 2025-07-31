package com.kh.moida.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.mapper.MoimMapper;
import com.kh.moida.mapper.ReviewMapper;
import com.kh.moida.model.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewMapper mapper;

	//후기 등록(write, edit 등록 합침): if로 나누기
	public int enrollReview(Review r, MultipartFile image) {
		return mapper.enrollReview(r, image);
		
	}
	//후기 수정페이지 이동
	public Review selectReview(int reviewId) {
		return mapper.selectReview(reviewId);
	}

}
