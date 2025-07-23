package com.kh.moida.controller;

import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MoimService moimService;

    @GetMapping("/")
    public String home(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        int offset = 0;

        List<Moim> moimList = moimService.findMoim(null, offset, 40);

        if (userPrincipal != null) {
            User loginUser = userPrincipal.getUser();
            model.addAttribute("loginUser", loginUser);
        }
        model.addAttribute("moimList", moimList);
        return "pages/home";
    }
}
