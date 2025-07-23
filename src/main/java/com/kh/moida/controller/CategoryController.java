package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.model.Moim;
import com.kh.moida.service.CategoryService;
import com.kh.moida.service.MoimService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final MoimService moimService;
    private static final int PAGE_SIZE = 30;

    @GetMapping({"/", "", "/{categoryId}"})
    public String categoryList(
            @PathVariable(name = "categoryId", required = false) Long categoryId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        int offset = (page - 1) * PAGE_SIZE;

        List<Category> categoryList = categoryService.getCategoryList();
        List<Moim> moimList = moimService.findMoim(categoryId, offset, PAGE_SIZE);

        String selectedCategoryName = (categoryId == null)
                ? "전체"
                : categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .map(Category::getCategoryName)
                .findFirst()
                .orElse("알 수 없음");
        int totalCount = moimService.countMoim(categoryId);
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("moimList", moimList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }
}
