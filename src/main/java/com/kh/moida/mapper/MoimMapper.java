package com.kh.moida.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kh.moida.notice.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.moida.model.Moim;
import com.kh.moida.model.User;

@Mapper
public interface MoimMapper {
    void updateMoimCategoryToDefault(Long categoryId);

    int insertMoim(Moim moim);

    List<Moim> findMoims(Map<String, Object> params);

    int countMoim(Long categoryId);

    Moim findMoimById(int moimId);


	void updateMoimWithoutFile(Moim moim);

	void updateMoimWithFile(Moim moim);

	int deleteMoimList(int moimId);
	
	//리뷰 리스트뽑기
	int countReview(int moimId);

	//조인 참여하기 
	void moimJoinMoim(@Param("moim") Moim moim, @Param("user") User user);


    int countHostedMoimCount(Long userId);

    List<Moim> findHostedMoim(Map<String, Object> params);

    int countJoinedMoim(Long userId);

    List<Moim> findManyJoinedMoim(Map<String, Object> params);

	void joinMoimCancel(@Param("moim") Moim moim, @Param("user") User user);

	int deleteMoim(int moimId);

    int moimquestion(Question question);
    

    ArrayList<Question> findQuestion(int moimId);

    int moimanswer(Question question);

	int cancelMoim(int moimId);

	int reviveMoim(int moimId);
}
