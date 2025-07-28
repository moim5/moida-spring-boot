package com.kh.moida.controller;

import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import com.kh.moida.service.MoimService;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    private final UserService userService;
    private final MoimService moimService;

    @GetMapping("/info")
    public String MyInfo(
            @AuthenticationPrincipal(expression = "user") User loginUser
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
            @RequestParam("prevPassword") String prevPassword,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
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

    @GetMapping("/moim/hosted/list")
    public String MyHostedMoimList(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        int totalCount = moimService.countHostedMoim(user.getUserId());
        int offset = (page - 1) * size;

        List<Moim> moimList = moimService.findManyHostedMoim(user.getUserId(), offset, size);

        model.addAttribute("moimList", moimList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", "hosted");

        return "pages/my/moim/list";
    }

    @GetMapping("/moim/joined/list")
    public String MyJoinedMoimList(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        int totalCount = moimService.countJoinedMoim(user.getUserId());
        int offset = (page - 1) * size;

        List<Moim> moimList = moimService.findManyJoinedMoim(user.getUserId(), offset, size);

        model.addAttribute("moimList", moimList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", "joined");

        return "pages/my/moim/list";
    }
}
