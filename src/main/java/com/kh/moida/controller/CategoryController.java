package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping({"/", ""})
    public String categoryList(
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("selectedCategoryName", "전체"); // 전체 선택 시
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }

    @GetMapping("/{categoryId}")
    public String categorySelectList(
            @PathVariable("categoryId") Long categoryId,
            HttpServletRequest request,
            Model model
    ) {
        List<Category> categoryList = categoryService.getCategoryList();
        String selectedCategoryName = categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .map(Category::getCategoryName)
                .findFirst()
                .orElse("알 수 없음");
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }
}
