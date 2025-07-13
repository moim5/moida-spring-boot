package com.kh.moida.controller;

import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class HomeController {
//    @GetMapping("/")
//    public String home(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            Model model
//    ) {
//        log.info("✅ HomeController 호출됨");
//        if (userPrincipal != null) {
//            User loginUser = userPrincipal.getUser();
//            model.addAttribute("loginUser", loginUser);
//        }
//        return "pages/home";
//    }
//
//    @GetMapping("/test")
//    @ResponseBody
//    public String test() {
//        log.info("🔥🔥🔥 /test 호출됨!");
//        return "ok";
//    }

    @GetMapping("/")
    @ResponseBody  // 임시로 추가
    public String home() {
        log.info("✅ HomeController / 호출됨");
        return "HOME PAGE WORKS!";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        log.info("🔥🔥🔥 /test 호출됨!");
        return "TEST PAGE WORKS!";
    }

}
