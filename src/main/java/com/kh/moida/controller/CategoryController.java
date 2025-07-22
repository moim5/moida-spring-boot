package com.kh.moida.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @GetMapping({"/", ""})
    public String categoryList(
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }

    @GetMapping("/{categoryId}")
    public String categorySelectList(
            @PathVariable("categoryId") Long categoryId,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }
}
