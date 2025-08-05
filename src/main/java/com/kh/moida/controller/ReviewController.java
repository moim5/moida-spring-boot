package com.kh.moida.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.kh.moida.dto.ReviewWithUser;
import com.kh.moida.model.UserPrincipal;
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

import com.kh.moida.model.Moim;
import com.kh.moida.model.Review;
import com.kh.moida.model.User;
import com.kh.moida.service.ReviewService;
import com.kh.moida.service.MoimService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService rService;
    private final MoimService moimService;

    // review_write view이동
    @GetMapping("/review/write/{moimId}")
    public String writeReview(
            @PathVariable("moimId") Long moimId,
            @AuthenticationPrincipal(expression = "user") User loginUser,
            Model model) {
        int result = rService.checkReview(moimId, loginUser.getUserId());
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        model.addAttribute("moimId", moimId);
        return "pages/my/review/review_write";
    }

    @PostMapping("/review/enroll")
    public String insertReview(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute Review r,
            @RequestParam(value = "imageUpload", required = false) MultipartFile image) throws IOException {
        // 1. 제약 걸기 : 리뷰작성자랑 모임 참여자랑 같은지 확인
        // 2. 제약 걸기 : 리뷰가 이미 등록되어 있나?
        // 2. 맞으면 등록 아니면 에러

        // 로그인 유저 ID 직접 세팅
        r.setUserId(loginUser.getUserId());
        int result = rService.checkReview(r.getMoimId(), loginUser.getUserId());
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int result2 = rService.existReview(r.getMoimId(), loginUser.getUserId());
        if (result2 > 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 3. 리뷰 등록하기
        int result3 = rService.writeReview(loginUser.getUserId(), r, image);
        if (result3 > 0) {
        	rService.updateAvgRate(r.getMoimId()); //별점 업데이트
            return "redirect:/review/detail/" + r.getMoimId();
            
        }
        return "redirect:/review/write/" + r.getMoimId();
    }

    // 후기 상세보기 read view이동
    @GetMapping("/review/detail/{moimId}")
    public String readReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("moimId") Long moimId,
            Model model
    ) {
        ArrayList<ReviewWithUser> reviewList = rService.getReview(moimId);
        Moim moim = moimService.findById(moimId);
   
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("moim", moim);
        if (userPrincipal != null) {
            model.addAttribute("loginUser", userPrincipal.getUser());
        } else {
            model.addAttribute("loginUser", null);
        }

        return "pages/my/review/review_read";
    }

    // 후기 수정 페이지 이동
    @GetMapping("/review/edit/{reviewId}")
    public String editReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("moimId") Long moimId,
            Model model,
            @AuthenticationPrincipal(expression = "user") User loginUser) {
        // 해당 리뷰가 존재하는지 확인
        // 1. 리뷰 데이터 조회
        Review review = rService.selectReview(reviewId);
        if (review == null) {
            return "redirect:/review/write/" + moimId;
        }

        // 2. 작성자 권한 확인
        int check = rService.checkReview(moimId, loginUser.getUserId());
        if (check == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 3. 뷰에 리뷰 데이터와 moimId 전달
        model.addAttribute("review", review);
        model.addAttribute("moimId", moimId);
        return "pages/my/review/review_edit";
    }

    // 후기 수정
    @PostMapping("/review/update")
    public String updateReview(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute Review r,
            @RequestParam(value = "imageUpload", required = false) MultipartFile image
    ) throws IOException {
        // 수정을 작업
        // (있는지 여부 판별) + 쓴 사람과 등록한 사람을 비교
        // 파일을 바꾸나? > 기존 파일을 삭제해야됨. 그리고서 파일을 업로드 하고, 그 이후 File DB를 쓰고, Review를 업데이트
        int result = rService.updateReview(loginUser.getUserId(), r, image);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정을 실패하였습니다.");
        } else {
        	rService.updateAvgRate(r.getMoimId()); //별점 업데이트
        }
        return "redirect:/review/read/" + r.getMoimId();
    }

    // 후기 삭제
    @PostMapping("/review/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId,
                               @AuthenticationPrincipal(expression = "user") User loginUser) {
        Review review = rService.selectReview(reviewId);
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다.");
        }
        if (review.getUserId() != loginUser.getUserId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        int result = rService.deleteReview(review);


        return "redirect:/pages/my/review/review_read";
    }
}