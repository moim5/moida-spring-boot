package com.kh.moida.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MoimController {
    @GetMapping("/createMoim")
    public String CreateMoim() {
        return "pages/moim/CreateMoim";
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

    @GetMapping("/moimDetail")
    public String moimDetail() {
        return "pages/moim/moim_detail";
    }

    @GetMapping("/moimReview/write")
    public String wrtieReview() {
        return "pages/moim/review/write";
    }

    @PostMapping("/moimReview/write")
    public String wrtieReview(@RequestParam String content) {
        //리뷰저장 로직 수행
        return "pages/moim/review/read";
    }
}
