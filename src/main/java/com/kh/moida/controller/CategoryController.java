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

    @GetMapping({"/", "", "/{categoryId}"})
    public String categoryList(
            @PathVariable(name = "categoryId", required = false) Long categoryId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        int offset = (page - 1) * size;

        List<Category> categoryList = categoryService.getCategoryList();
        List<Moim> moimList = moimService.findMoim(categoryId, offset, size);

        String selectedCategoryName = (categoryId == null)
                ? "전체"
                : categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .map(Category::getCategoryName)
                .findFirst()
                .orElse("알 수 없음");
        int totalCount = moimService.countMoim(categoryId);

        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("baseUrl", "/category" + (categoryId != null ? "/" + categoryId : ""));
        model.addAttribute("moimList", moimList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("requestURI", request.getRequestURI());
        return "pages/category/list";
    }
}
