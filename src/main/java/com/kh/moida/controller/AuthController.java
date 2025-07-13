package com.kh.moida.controller;

import com.kh.moida.config.JwtUtil;
import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign")
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @GetMapping("/up")
    public String showRegisterForm() {
        return "pages/sign/up/signUp";
    }

    @PostMapping("/up")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirm,
            @RequestParam String name,
            @RequestParam String birthday,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String zipcode,
            @RequestParam String address1,
            @RequestParam String address2,
            Model model
    ) {
        if (!password.equals(confirm)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "pages/sign/up/signUp";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setPhone(phone);
        user.setZipCode(zipcode);
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setGender(gender.charAt(0));
        user.setBirthday(Date.valueOf(birthday));
        user.setIsActive('Y');
        user.setIsAdmin('N');
        userService.save(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/in")
    public String showLoginForm() {
        return "pages/sign/in/signIn";
    }

    @PostMapping("/in")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            Model model
    ) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(auth);

            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            String jwt = jwtUtil.generateToken(userPrincipal);

            Cookie cookie = new Cookie("JWT_TOKEN", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            return "redirect:/";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "사용자명 또는 비밀번호가 올바르지 않습니다.");
            return "pages/sign/in/signIn";
        }
    }

    @GetMapping("/out")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return "redirect:/sign/in";
    }
}
