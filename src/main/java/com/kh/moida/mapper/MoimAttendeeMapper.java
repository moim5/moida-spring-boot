package com.kh.moida.mapper;

import java.util.List;
import java.util.Map;

import com.kh.moida.dto.MoimAttendeeWithUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MoimAttendeeMapper {

	int searchMoimAttendee(Map<String, Object> params);

	int joinMoim(Map<String, Object> params);

	int joinMoimCancel(Map<String, Object> params);

	List<MoimAttendeeWithUser> findMoimAttendee(Long moimId);
}
