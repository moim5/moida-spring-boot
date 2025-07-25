package com.kh.moida.controller;

import com.kh.moida.model.Category;
import com.kh.moida.model.User;
import com.kh.moida.service.CategoryService;
import com.kh.moida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    private static final int PAGE_SIZE = 20;

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

    @GetMapping("/user/list")
    public String UserList(
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        int offset = (page - 1) * PAGE_SIZE;
        List<User> userList = userService.findUser(offset, PAGE_SIZE);
        model.addAttribute("userList", userList);
        return "pages/admin/user/list";
    }

    @GetMapping("/user/modify/{userId}")
    public String UserModify(
            @PathVariable("userId") Long userId,
            Model model
    ) {
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user", user);
        return "pages/admin/user/detail";
    }
    
    @GetMapping("/pages/admin/moimList/moimList")
    public String moimList() {
    	//model에서 보낸 method로 데이터 받아서 삭제처리 후 현재페이지로 redirect하기
    	return null;
    }
    
    
    
    
    
    
    
    
    
    
    
}
