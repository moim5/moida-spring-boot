package com.kh.moida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoimController {
    @GetMapping("/createMoim")
    public String CreateMoim() {
    	return "pages/CreateMoim";
    }
    
    @GetMapping("/")
    public String JoinMoim() {
    	return "pages/JoinMoim";
    }
    
    @GetMapping("/")
    public String modifyMoim() {
    	return "pages/modifyMoim";
    }
    
    @GetMapping("/")
    public String personalInfo() {
    	return "pages/personalInfo";
    }

}
