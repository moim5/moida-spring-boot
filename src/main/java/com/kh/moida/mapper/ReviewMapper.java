package com.kh.moida.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.kh.moida.model.Review;

@Mapper
public interface ReviewMapper {

	public ArrayList<Review> getReviewList();

}
