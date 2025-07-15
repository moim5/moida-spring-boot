package com.kh.moida.controller;

import com.kh.moida.model.User;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    private final UserService userService;

    @GetMapping("/info")
    public String MyInfo(
            @AuthenticationPrincipal(expression = "user") User user,
            Model model
    ) {
        return "pages/my/info";
    }

    @PostMapping("/info")
    public String UpdateInfo(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute User user,
            Model model
    ) {
        user.setUserId(loginUser.getUserId());
        userService.updateUserInfo(user);

        return "redirect:/my/info";
    }
}
