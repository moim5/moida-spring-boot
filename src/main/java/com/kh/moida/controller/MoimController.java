package com.kh.moida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.moida.model.Moim;
import com.kh.moida.service.MoimService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MoimController {
//	private final MoimService moimService;
	
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

    
    //모임 상세보기 view이동
    @GetMapping("/moimDetail")
    public String moimDetail() {
        return "pages/moim/moim_detail";
    }
    
    //모임 신청하기 (moimId만 url에 정보 담고 서버에서 DB조회해서 데이터 뽑아오기
    @PostMapping("/moimEnroll")
    public String enrollMoim(@ModelAttribute Moim moim) {
//    	moimService.enrollMoim(moim);
    	return "redirect:/pages/moim/moim_datil?moimId=" + moim.getMoimId();
    }
    @GetMapping("/moimDetail")
    public String moimDetail(@RequestParam("moimId")int moimId, Model model ) {
//    	Moim moim = moimService.findById(moimId);
//    	model.addAttribute("moim", moim);
    	return "pages/moim/moim_detail";
    }

   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
