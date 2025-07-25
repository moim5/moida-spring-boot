package com.kh.moida.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.exception.MoimException;
import com.kh.moida.model.Category;
import com.kh.moida.model.User;
import com.kh.moida.service.CategoryService;
import com.kh.moida.service.MoimService;
import com.kh.moida.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final MoimService mService;
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
    
    @GetMapping("/moimList/delete")
    public String deleteMoimList(@RequestParam("moimId")int moimId, Model model) {
    	//update로 스테이터스 N으로 변경
    	int result = mService.deleteMoimList(moimId);
    	if(result > 0) {
    		model.addAttribute("msg","모임이 삭제 되었습니다.");
    		model.addAttribute("url","/pages/admin/moimList/moimList");
    		return "views/common/sendRedirect";
    	} else {
    		throw new MoimException("모임 삭제를 실패하였습니다.");
    	}
    	
    }
    
 
    
    
    
    
}
