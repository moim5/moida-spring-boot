package com.kh.moida.controller;

import com.kh.moida.model.User;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    private final UserService userService;

    @GetMapping("/info")
    public String MyInfo(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        return "pages/my/info";
    }

    @PostMapping("/info")
    public String UpdateInfo(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @ModelAttribute User user
    ) {
        user.setUserId(loginUser.getUserId());
        userService.updateUserInfo(user);

        return "redirect:/my/info";
    }

    @GetMapping("/password")
    public String MyPassword(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        return "pages/my/password";
    }

    @PostMapping("/password")
    public String UpdatePassword(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam String prevPassword,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "입력하신 새로운 비밀번호가 일치하지 않습니다.");
            return "pages/my/password";
        }

        boolean result = userService.updatePassword(user.getUserId(), prevPassword, password);
        if (!result) {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다");
            return "pages/my/password";
        }

        return "redirect:/my/password";
    }
}
