package com.kh.moida.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.kh.moida.model.Review;

@Mapper
public interface ReviewMapper {

	//리뷰리스트뽑기
	public ArrayList<Review> getReviewList();

}
