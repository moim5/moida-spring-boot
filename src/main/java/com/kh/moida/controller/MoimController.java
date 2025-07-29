package com.kh.moida.controller;

import java.util.ArrayList;
import java.util.Objects;

import com.kh.moida.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.model.Moim;
import com.kh.moida.model.Review;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.MoimService;
import com.kh.moida.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

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
            @ModelAttribute Moim moim,
            @RequestParam("moimImage") MultipartFile moimImage,
            @AuthenticationPrincipal UserPrincipal loginUser,
            Model model
    ) {
        try {
            Moim createdMoim = moimService.insertMoim(loginUser.getUser(), moim, moimImage);
            return "redirect:/moim/" + createdMoim.getMoimId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/moim/create";
        }
    }

    @GetMapping("/modify/{moimId}")
    public String MoimModify(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @PathVariable("moimId") int moimId,
            Model model
    ) {
        Moim moim = moimService.findById(moimId);
        if (moim == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(moim.getUserId(), loginUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        model.addAttribute("moim", moim);
        return "pages/moim/modify";
    }

    @PostMapping("/update")
    public String MoimUpdate(
            @ModelAttribute Moim moim,
            MultipartFile moimImage,
            @AuthenticationPrincipal(expression = "user") User loginUser,
            Model model
    ) {
        Moim prevMoim = moimService.findById(moim.getMoimId());
        if (prevMoim == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(moim.getUserId(), loginUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        try {
            moimService.moimUpdate(moim, moimImage);
            return "redirect:/my/moim/hosted/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/moim/modify/" + moim.getMoimId();
        }
    }

    //모임 삭제
    @GetMapping("/deleteMoim")
    public String MoimDelete(Moim moim,@RequestParam("moimId") int moimId) {
        //is_visible N으로 업데이트
        int result = moimService.deleteMoim(moimId);
        if (result > 0) {

        }
        return "";
    }


    @GetMapping("/joinMoim/{moimId}") //모임 참여
    public String JoinMoim(
    		@AuthenticationPrincipal UserPrincipal loginUser,
    		Moim moim,
    		@PathVariable("moimId") int moimId) {
    	moimService.moimJoinMoim(moim,loginUser.getUser());
        return "pages/moim/joinMoim";
    }

    //모임 참가신청 취소
    //참가 신청할 모임 id 
    @GetMapping("/joinMoimCancel")
    public String joinMoimCancel(
            @AuthenticationPrincipal UserPrincipal loginUser,
            @ModelAttribute Moim moim
    ) {
        moimService.joinMoimCancel(moim, loginUser.getUser());
        return "";
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
    @GetMapping("/moim/moim_detail/{moimId}")
    public String reviewList(@PathVariable int moimId, Model model) {
        // new ReviewService()는  위에 final로 의존성 해둠
        ArrayList<Review> reviewList = rService.getReviewList(moimId);
        model.addAttribute("reviewList", reviewList);

        return "detail";
    }
}
