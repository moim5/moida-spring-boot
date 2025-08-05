package com.kh.moida.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;


import com.kh.moida.dto.ReviewWithUser;
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

    //평균 별점 계산 후 업데이트하기
    public void updateAvgRate(Long moimId) {
    	Double avgRate = mapper.getRateAvgByMoimId(moimId);
    	if(avgRate == null) {
    		avgRate = 0.0;
    	}
    	 Map<String, Object> map = new HashMap<>();
    	    map.put("moimId", moimId);
    	    map.put("avgRate", avgRate);

    	    moimMapper.updateAvgRate(map);
    }

    //후기등록(체크: 참가한 모임이 있는지)
    public int checkReview(Long moimId, Long userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("moimId", moimId);
        map.put("userId", userId);
        return moimMapper.existMoimAttendee(map);
    }

    //후기 등록 (체크 : 작성한 리뷰가 있는지)
    public int existReview(Long moimId, Long userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("moimId", moimId);
        map.put("userId", userId);
        return mapper.existWriter(map);
    }

    //후기 등록하기 (파일이 있으면 선 등록 후 리뷰 등록하기)
    public int writeReview(Long userId, Review r, MultipartFile image) throws IOException {
        // 파일을 업로드를 하고, DB File을 씀
        if (image != null && !image.isEmpty()) {
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
        }
        r.setUserId(userId);
        // review 등록하기
        int result = mapper.writeReview(r);
        
        //review등록 성공 시 평균 별점 업데이트
        if(result > 0) {
        	 Double avgRate = mapper.getRateAvgByMoimId(r.getMoimId());

        	    Map<String, Object> map = new HashMap<>();
        	    map.put("moimId", r.getMoimId());
        	    map.put("avgRate", avgRate);

        	    moimMapper.updateAvgRate(map);
        }
        return result;
    }

	 // 후기읽기 페이지용 리뷰가져오기
    public ArrayList<ReviewWithUser> getReview(Long moimId) {
        return mapper.getReview(moimId);
    }

    //후기 수정 페이지->리뷰가져오기
    public Review selectReview(Long reviewId) {
        return mapper.selectReview(reviewId);
    }


    //후기 수정등록
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

    // 후기삭제
    public int deleteReview(Review review) {
        // 1. 파일 삭제 (fileId, fileConvert)
        String s3Key = review.getFileConvert();
        if (s3Key != null && !s3Key.isEmpty()) {
            fileUploadService.deleteFile(s3Key);
        }

        // File 삭제
        fileMapper.deleteFile(review.getFileId());

        // Review삭제 (reviewId)
        return mapper.deleteReview(review.getReviewId());
    }

   
   

}
