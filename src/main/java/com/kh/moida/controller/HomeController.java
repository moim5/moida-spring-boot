package com.kh.moida.controller;

import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        if (userPrincipal != null) {
            User loginUser = userPrincipal.getUser();
            model.addAttribute("loginUser", loginUser);
        }
        return "pages/home";
    }
}
