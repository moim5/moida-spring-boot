package com.kh.moida.controller;

import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.model.Question;
import com.kh.moida.service.MoimService;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        int totalCount = moimService.countHostedMoim(user.getUserId());
        int offset = (page - 1) * size;

        List<Moim> moimList = moimService.findManyHostedMoim(user.getUserId(), offset, size);

        model.addAttribute("moimList", moimList);
        model.addAttribute("baseUrl", "/moim/hosted/list");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", "hosted");
        model.addAttribute("now", new Date());
//        System.out.println(moimList);
        return "pages/my/moim/list";
    }

    @GetMapping("/moim/joined/list")
    public String MyJoinedMoimList(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        int totalCount = moimService.countJoinedMoim(user.getUserId()); //참가자 수
        int offset = (page - 1) * size;

        List<Moim> moimList = moimService.findManyJoinedMoim(user.getUserId(), offset, size);

        

        model.addAttribute("moimList", moimList);
        model.addAttribute("baseUrl", "/moim/joined/list");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", "joined");
        model.addAttribute("now", new Date());


        return "pages/my/moim/list";
    }

    @GetMapping("/qna/list")
    public String MyQnAList(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User loginUser = null;
        int isMoimAttendee = 0;
        if (userPrincipal != null) {
            loginUser = userPrincipal.getUser();
        }
        ArrayList<Question> myquestions = moimService.findMyQuestion(loginUser.getUserId());
        model.addAttribute("myquestions", myquestions);
        return "pages/my/qna/list";

    }
}
