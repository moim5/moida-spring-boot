package com.kh.moida.controller;

import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.MoimService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kh.moida.model.Moim;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/moim")
@RequiredArgsConstructor
public class MoimController {
    private final MoimService moimService;

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
        System.out.println("모임 생성 요청 들어옴!");

        try {
            moimService.insertMoim(loginUser.getUser(), moim, moimImage);
            return "redirect:/my/moim";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/moim/create";
        }
    }

    @GetMapping("/joinMoim")
    public String JoinMoim() {
        return "pages/moim/JoinMoim";
    }

    @GetMapping("/modifyMoim")
    public String modifyMoim() {
        return "pages/moim/modifyMoim";
    }

    @GetMapping("/moimAdminPage")
    public String moimAdminPage() {
        return "pages/moim/moimAdminPage";
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

    @GetMapping("/{moimId}")
    public String MoimDetail(
            @PathVariable("moimId") int moimId,
            Model model
    ) {
        Moim moim = moimService.findById(moimId);
        model.addAttribute("moim", moim);
        return "pages/moim/detail";
    }
}
