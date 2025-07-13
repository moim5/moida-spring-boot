package com.kh.moida.controller;

import com.kh.moida.model.User;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public String profile(
            @AuthenticationPrincipal(expression = "user") User user, Model model
    ) {
        model.addAttribute("user", user);
        return "pages/my/profile/profile";
    }
}
