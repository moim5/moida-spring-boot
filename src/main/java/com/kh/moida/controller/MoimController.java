package com.kh.moida.controller;

import java.util.ArrayList;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.model.Moim;
import com.kh.moida.model.Review;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.MoimService;
import com.kh.moida.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/moim")
@RequiredArgsConstructor
public class MoimController {
    private final MoimService moimService;
    private final ReviewService rService;

    @GetMapping("/create")
    public String MoimCreate() {
        return "pages/moim/create";
    }

    @PostMapping("/insert")
    public String MoimWrite(
            Moim moim,
            MultipartFile moimImage,
            @AuthenticationPrincipal UserPrincipal loginUser,
            Model model
    ) {
        try {
            moimService.insertMoim(loginUser.getUser(), moim, moimImage);
            return "redirect:/my/moim";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/moim/create";
        }
    }
    
    @PostMapping("/modifyMoim") //모임 수정
    public String MoimUpdate(
    		Moim moim,
    		MultipartFile moimImage,
    		@AuthenticationPrincipal UserPrincipal loginUser,
    		Model model) {
    	moimService.moimUpdate(moim, moimImage, loginUser.getUser());
    	return "pages/moim/modifyMoim";
    }

    @GetMapping("/joinMoim") //모임 참여
    public String JoinMoim() {
        return "pages/moim/JoinMoim";
    }

//    @GetMapping("/modifyMoim")
//    public String modifyMoim() {
//        return "pages/moim/modifyMoim";
//    }

    @GetMapping("/moimAdminPage")
    public String moimAdminPage() {
        return "pages/admin/moim/list";
    }

    @GetMapping("/personalInfo")
    public String personalInfo() {
        return "pages/personalInfo";
    }

    //모임 신청하기 (moimId만 url에 정보 담고 서버에서 DB조회해서 데이터 뽑아오기
    @PostMapping("/moimEnroll")
    public String enrollMoim(@ModelAttribute Moim moim) {
//    	moimService.enrollMoim(moim);
        return "redirect:/pages/moim/moim_datil?moimId=" + moim.getMoimId();
        
    }

    //moim_detail이동
    @GetMapping("/{moimId}")
    public String MoimDetail(
            @PathVariable("moimId") int moimId,
            Model model
    ) {
        Moim moim = moimService.findById(moimId);
        model.addAttribute("moim", moim);
        return "pages/moim/detail";
    }
    
    
    //reviewList뽑기
    @GetMapping("/pages/moim/moim_detail/{moimId}")
    public String reviewList(@PathVariable int moimId, Model model) {
        // new ReviewService()는  위에 final로 의존성 해둠
        ArrayList<Review> reviewList = rService.getReviewList(moimId);
        model.addAttribute("reviewList", reviewList);

        return "detail";
    }
}
