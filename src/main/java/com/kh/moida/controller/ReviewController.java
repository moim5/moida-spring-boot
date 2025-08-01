package com.kh.moida.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.kh.moida.model.Review;
import com.kh.moida.model.User;
import com.kh.moida.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService rService;


    
    //review_write view이동
    @GetMapping("/review/write/{moimid}")
    public String writeReview(
    		@RequestParam("moimId")Long moimId,
    		@AuthenticationPrincipal(expression = "user") User loginUser
    ) {
    	int result = rService.checkReview(moimId, loginUser.getUserId());
    	if (result == 0) {
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    	}
		return "pages/my/review/review_write";
    }

    @PostMapping("/review/enroll")
    public String insertReview(
    		@AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute Review r,
            @RequestParam(value="imageUpload",required = false) MultipartFile image
    ) throws IOException {
    	//1. 제약 걸기 : 리뷰작성자랑 모임 참여자랑 같은지 확인
    	//2. 제약 걸기 : 리뷰가 이미 등록되어 있나?
    	//2. 맞으면 등록 아니면 에러
    	int result = rService.checkReview(r.getMoimId(), loginUser.getUserId());
    	if (result == 0) {
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    	}
    	int result2 = rService.existReview(r.getMoimId(), loginUser.getUserId());
    	if (result2 == 0) {
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    	}
    	
    	
    	int result3 = rService.writeReview(loginUser.getUserId(), r, image);
    	if(result3 > 0) {
    		return "redirect:/review/detail/" + r.getReviewId();
    	}
    	return "redirect:/review/write/" + r.getMoimId();
    }
    

    //후기 상세보기 read view이동
    @GetMapping("/review/detail/{reviewId}")
    public String readReview(
    		@AuthenticationPrincipal(expression = "user") User loginUser,
    		@PathVariable Long reviewId,
    		Model model
    ) {
    	Review result = rService.readReview(reviewId);
    	
    	model.addAttribute("review", result);
    	model.addAttribute("loginUser", loginUser);
        return "pages/my/review/review_read";
    }
    
    //후기 수정 페이지 이동(리뷰아이디로 조회 후 데이터보내주기)
    @GetMapping("/review/edit/{reviewId}")
    public String editReview(@PathVariable int reviewId, Model model) {
    	Review r = rService.selectReview(reviewId);
    	model.addAttribute("review",r);
    	return "pages/my/review/review_edit";
    }	
    
    @PostMapping("/review/update")
    public boolean updateReview(
    		@AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute Review r,
            @RequestParam(value="imageUpload",required = false) MultipartFile image
    ) throws IOException {
    	// 수정을 작업
    	// (있는지 여부 판별) + 쓴 사람과 등록한 사람을 비교
    	// 파일을 바꾸나? > 기존 파일을 삭제해야됨. 그리고서 파일을 업로드 하고, 그 이후 File DB를 쓰고, Review를 업데이트
    	int result = rService.updateReview(loginUser.getUserId(), r, image);
    	if (result == 0) {
    		return false;
    	}
    	return true;
    }
    
	
    
    //후기 삭제하기(수정중)
    @PostMapping("/review/delete/{reviewId}")
    public String deleteReview() {
    	return "redirect:/pages/my/review/review_read";
    }

}
