package com.kh.moida.service;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.model.File;
import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import org.springframework.stereotype.Service;

import com.kh.moida.mapper.MoimMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MoimService {
    private final MoimMapper moimMapper;
    private final FileMapper fileMapper;
    private final FileUploadService fileUploadService;

    public void insertMoim(User loginUser, Moim moim, MultipartFile moimImage) throws IOException {
        String originalName = moimImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "moim/" + timestamp + "." + ext;

        fileUploadService.uploadFile(moimImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);

        fileMapper.insertFile(file);

        moim.setUserId(loginUser.getUserId());
        moim.setFileId(file.getFileId());
        moim.setFileOrigin(originalName);
        moim.setFileConvert(newName);
        moim.setIsVisible("Y");
        moim.setIsActive("Y");
        moimMapper.insertMoim(moim);
    }

	public List<Moim> findMoim(Long categoryId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);
        return moimMapper.findMoims(params);
	}

	public int countMoim(Long categoryId) {
		return moimMapper.countMoim(categoryId);
	}

	//모임 아이디찾기
    public Moim findById(int moimId) {
        return moimMapper.findMoimById(moimId);
    }

    //admin:모임삭제
	public int deleteMoimList(int moimId) {
		return moimMapper.deleteMoimList(moimId);
	}
	
	//해당하는 모임에 대한 리뷰 리스트 출력
	//리뷰 이미 작성했을 경우 write view이동 막기->모임아이디 기준으로 찾아야돼서 맵퍼 다르게하기
	 public int countReview(int moimId) {
		 return moimMapper.countReview(moimId);
	  }

    public int countHostedMoim(Long userId) {
        return moimMapper.countHostedMoimCount(userId);
    }

    public List<Moim> findManyHostedMoim(Long userId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findHostedMoim(params);

    }

    public int countJoinedMoim(Long userId) {
        return moimMapper.countJoinedMoim(userId);
    }

    public List<Moim> findManyJoinedMoim(Long userId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findManyJoinedMoim(params);
    }
}
