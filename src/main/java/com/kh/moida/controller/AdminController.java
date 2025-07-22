package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            Model model
    ) {
        try {
            categoryService.insertCategory(category, categoryImage);
            return "redirect:/admin/category/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/admin/category/write";
        }
    }

    @GetMapping("/category/modify/{categoryId}")
    public String CategoryModify(
            @PathVariable("categoryId") Long categoryId,
            Model model
    ) {
        Category category = categoryService.getCategory(categoryId);
        model.addAttribute("category", category);
        return "pages/admin/category/modify";
    }

    @PostMapping("/category/update/{categoryId}")
    public String CategoryUpdate(
            @PathVariable("categoryId") Long categoryId,
            Category category,
            MultipartFile categoryImage,
            Model model
    ) {
        Category existCategory = categoryService.getCategory(categoryId);
        if (existCategory == null) {
            return "redirect:/admin/category/list";
        }
        try {
            category.setCategoryId(categoryId);
            categoryService.updateCategory(category, categoryImage, existCategory);
            return "redirect:/admin/category/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("category", existCategory);
            return "pages/admin/category/modify";
        }
    }

    @PostMapping("/category/delete/{categoryId}")
    public String CategoryDelete(
            @PathVariable("categoryId") Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/admin/category/list";
    }
}
