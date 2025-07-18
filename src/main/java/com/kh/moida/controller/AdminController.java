package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CategoryService categoryService;

    @GetMapping("/category/list")
    public String CategoryList(Model model) {
        List<Category> list = categoryService.getCategoryList();
        model.addAttribute("list", list);
        return "pages/admin/category/list";
    }

    @GetMapping("/category/write")
    public String CategoryWrite() {
        return "pages/admin/category/write";
    }

    @PostMapping("/category/insert")
    public String CategoryInsert(
            Category category,
            MultipartFile categoryImage,
            Model model,
            HttpServletRequest request
    ) {
        try {
            categoryService.insertCategory(category, categoryImage);
            return "redirect:/admin/category/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/admin/category/write";
        }
    }
}
