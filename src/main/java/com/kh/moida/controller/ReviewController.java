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
import org.springframework.web.servlet.ModelAndView;

import com.kh.moida.model.Review;
import com.kh.moida.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService rService;

    //후기 쓰기 view이동
    @GetMapping("/review/write")
    public String writeReview() {
        return "pages/my/review/review_write";
    }

    //후기 등록
    @PostMapping("/review/enroll")
    public void insertReview(
            @ModelAttribute Review review,
            @RequestParam("imageUpload") MultipartFile image
    ) {
        //파일을 .. 서버에 저장하고싶은데요.
//    	String fileName = image.getOriginalFilename();
//    	rService.enrollReview(review, fileName);
//        return "mv";
    }

    //후기 상세보기 view이동
    @GetMapping("/review/detail")
    public String readReview() {
        return "pages/my/review/read";
    }

    //reviewList (moimDetail에서 보여줄 거..)
    @GetMapping("/pages/moim/moim_detail/{moimId}")
    public String reviewList(@PathVariable int moimId, Model model) {
        //review list가져오는 로직
        ArrayList<Review> reviewList = rService.getReviewList();
        model.addAttribute("reviewList", reviewList);

        return "pages/moim/moim_detail";

    }


}
