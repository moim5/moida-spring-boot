package com.kh.moida.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addUserToModel(
            @AuthenticationPrincipal UserDetails user,
            Model model
    ) {
        if (user != null) {
            model.addAttribute("loginUser", user);
        }
    }
}
