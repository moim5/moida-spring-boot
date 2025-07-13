package com.kh.moida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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

    @GetMapping("/personalInfo")
    public String personalInfo() {
        return "pages/personalInfo";
    }

    @GetMapping("/moimDetail")
    public String moimDetail(){
        return "pages/moim/moimDetail";
    }
    
    @GetMapping("/moimReview/write")
    public String wrtieReview(){
        return "pages/moim/review/write";
    }

    @PostMapping("/moimReview/write")
    public String wrtieReview(){
        //리뷰저장 로직 수행
        return "pages/moim/review/read";
    }
   
  
}
