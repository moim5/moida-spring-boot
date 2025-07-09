package com.kh.moida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoimController {
    @GetMapping("/createMoim")
    public String CreateMoim() {
    	return "pages/moim/CreateMoim";
    }
    
    @GetMapping("/")
    public String JoinMoim() {
    	return "pages/moim/JoinMoim";
    }
    
    @GetMapping("/")
    public String modifyMoim() {
    	return "pages/moim/modifyMoim";
    }
    
    @GetMapping("/")
    public String personalInfo() {
    	return "pages/personalInfo";
    }

}
