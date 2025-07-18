package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final CategoryService categoryService;

    @ModelAttribute
    public void addUserToModel(
            @AuthenticationPrincipal UserDetails user,
            Model model
    ) {
        if (user != null) {
            model.addAttribute("loginUser", user);
        }
    }

    @ModelAttribute("categoryList")
    public List<Category> categoryList() {
        return categoryService.getCategoryList();
    }
}
