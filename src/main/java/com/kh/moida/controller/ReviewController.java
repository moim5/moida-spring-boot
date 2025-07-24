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
    
    //후기 쓰기 view이동(이미 작성완료했을 경우 현재 페이지 리다이렉트)
    @GetMapping("/review/write")
    public String writeReview(@RequestParam("reviewId")int reviewId) {
        int result = rService.countReview(reviewId);
        if(result > 0) {
        	return "redirect:/my/review/list?msg=exist"; //할일 : 이미 후기를 작성하셨습니다 알럿창 띄우기(js)
        }
    	return "pages/my/review/review_write";
    }

    //후기 등록
    @PostMapping("/review/enroll")
    public void insertReview(
            @ModelAttribute Review review,
            @RequestParam("imageUpload") MultipartFile image
    ) {
        //파일을 .. 서버에 저장하고싶은데요.

    }

    //후기 상세보기 read view이동
    @GetMapping("/review/detail")
    public String readReview() {
        return "pages/my/review/review_read";
    }

    
    //후기write 등록 : read view 이동
    
    //후기 작성view
 /*   @GetMapping("review/edit")
    public ModelAndView editReview(@ModelAttribute Review r, ModelAndView mv) {
    	
    	return "pages/my/review/review_edit";
    }		

    //후기 수정
    @PostMapping("review/update")
    public void 	*/

}
