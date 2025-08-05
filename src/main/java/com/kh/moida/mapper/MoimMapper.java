package com.kh.moida.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.moida.model.Question;
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

    Moim findMoimById(Long moimId);


	void updateMoimWithoutFile(Moim moim);

	void updateMoimWithFile(Moim moim);

	int deleteMoimList(int moimId);
	
	//조인 참여하기 
	void moimJoinMoim(@Param("moim") Moim moim, @Param("user") User user);


    int countHostedMoimCount(Long userId);

    List<Moim> findHostedMoim(Map<String, Object> params);

    int countJoinedMoim(Long userId);

    List<Moim> findManyJoinedMoim(Map<String, Object> params);

	int deleteMoim(int moimId);

    int moimquestion(Question question);
    
    ArrayList<Question> findQuestion(Long moimId);

    ArrayList<Question> findMyQuestion(Long userId);

    int moimanswer(Question question);

	int cancelMoim(Long moimId);

	int reviveMoim(Long moimId);

	int existMoimAttendee(HashMap<String, Object> map);

    int countMoimForAdmin();
    
    //별점 업데이트
    void updateAvgRate(Map<String, Object> map);

	ArrayList<Moim> findManyMoimForAdmin(Map<String, Object> params);

    int questionDelete(int quesId);

    int answerDelete(int quesId);


	
}
