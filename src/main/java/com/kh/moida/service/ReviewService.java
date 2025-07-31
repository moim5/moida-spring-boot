package com.kh.moida.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.mapper.MoimMapper;
import com.kh.moida.mapper.ReviewMapper;
import com.kh.moida.model.File;
import com.kh.moida.model.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final FileUploadService fileUploadService;
	private final ReviewMapper mapper;
	private final MoimMapper moimMapper;
	private final FileMapper fileMapper;

	//후기 수정페이지 이동
	public Review selectReview(int reviewId) {
		return mapper.selectReview(reviewId);
	}

	//후기등록( 체크)
	public int checkReview(Long moimId, Long userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("moimId", moimId);
		map.put("userId", userId);
		return moimMapper.existMoimAttendee(map); 
	}
	public int existReview(Long moimId, Long userId) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("moimId", moimId);
		map.put("userId", userId);
		return mapper.existWriter(map);
	}

	//후기 등록
	public int writeReview(Long userId, Review r, MultipartFile image) throws IOException {
		// 파일을 업로드를 하고, DB File을 씀
		
		  String originalName = image.getOriginalFilename();
	        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
	        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	        String newName = "review/" + timestamp + "." + ext;

	        fileUploadService.uploadFile(image, newName);

	        File file = new File();
	        file.setFileOrigin(originalName);
	        file.setFileConvert(newName);
	        fileMapper.insertFile(file);

	        r.setFileId(file.getFileId());
	        r.setUserId(userId);
		//review DB
	        return mapper.writeReview(r);
	}

	public Review readReview(Long reviewId) {
		return mapper.readReview(reviewId);
	}

	public int updateReview(Long userId, Review r, MultipartFile image) throws IOException {
		// 1. 쓴 사람과 원래 있는 리뷰 작성자가 같은가
		HashMap<String, Object> map = new HashMap<>();
		map.put("moimId", r.getMoimId());
		map.put("userId", userId);
		int result = mapper.existWriter(map);
		if (result == 0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 파일이 있는가
		if (image == null) {
			mapper.updateReviewWithoutFile(r);
		} else {
			String originalName = image.getOriginalFilename();
	        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
	        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	        String newName = "review/" + timestamp + "." + ext;

	        fileUploadService.uploadFile(image, newName);

	        File file = new File();
	        file.setFileOrigin(originalName);
	        file.setFileConvert(newName);
	        fileMapper.insertFile(file);
			
	        r.setFileId(file.getFileId());
	        mapper.updateReviewWithFile(r);
		}
		return 1;
	}

	
}
