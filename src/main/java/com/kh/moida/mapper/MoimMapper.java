package com.kh.moida.mapper;

import com.kh.moida.model.Moim;
import com.kh.moida.model.User;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MoimMapper {
    void updateMoimCategoryToDefault(Long categoryId);

    void insertMoim(Moim moim);

    List<Moim> findMoims(Map<String, Object> params);

    int countMoim(Long categoryId);

    Moim findMoimById(int moimId);


	void updateMoimWithoutFile(Moim moim);

	void updateMoimWithFile(Moim moim);

	int deleteMoimList(int moimId);
	
	//리뷰 리스트뽑기
	int countReview(int moimId);

	void moimJoinMoim(Moim moim, User user);

}
