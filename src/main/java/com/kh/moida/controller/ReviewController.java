package com.kh.moida.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.model.Review;
import com.kh.moida.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService rService;

    //reviewList
    @GetMapping("/pages/moim/moim_detail/{moimId}")
    public String reviewList(@PathVariable int moimId, Model model) {
        ArrayList<Review> reviewList = rService.getReviewList();
        model.addAttribute("reviewList", reviewList);

        return "detail";
    }
    
    //review_write view이동(이미 작성완료했을 경우 현재 페이지 리다이렉트)
    @GetMapping("/review/write")
    public String writeReview(@RequestParam("reviewId")int reviewId) {
        int result = rService.countReview(reviewId);
        if(result > 0) {
        	return "redirect:/my/review/list?msg=exist"; //할일 : 이미 후기를 작성하셨습니다 알럿창 띄우기(js)
        }
    	return "pages/my/review/review_write";
    }

    //후기 write + edit-> enroll+update 합치기
    @PostMapping({"/review/enroll","/review/update"})
    public String insertReview(
            @ModelAttribute Review r,
            @RequestParam(value="imageUpload",required = false)
            Model model, MultipartFile image) {
        //파일을 .. 서버에 저장하고싶은데요.
    	int result = rService.enrollReview(r,image);
    	
    	model.addAttribute("meg","후기 등록을 성공하였습니다.");
    	return "redirect:/pages/my/review/review_read";
    }

    //후기 상세보기 read view이동
    @GetMapping("/review/detail")
    public String readReview() {
        return "pages/my/review/review_read";
    }

    //후기 수정 페이지 이동(리뷰아이디로 조회 후 데이터보내주기)
    @PostMapping("/review/edit/{reviewId}")
    public String editReview(@PathVariable int reviewId, Model model) {
    	Review r = rService.selectReview(reviewId);
    	model.addAttribute("review",r);
    	return "pages/my/review/review_edit";
    }		
    
    //후기 삭제하기(수정중_
    @PostMapping("/review/delete/{reviewId}")
    public String deleteReview() {
    	return "redirect:/pages/my/review/review_read";
    }

}
