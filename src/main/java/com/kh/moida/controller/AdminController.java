package com.kh.moida.controller;

import java.util.ArrayList;
import java.util.List;

import com.kh.moida.model.Moim;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final MoimService moimService;

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
            @ModelAttribute Category category,
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
            @ModelAttribute Category category,
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
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        int offset = (page - 1) * size;
        List<User> userList = userService.findUser(offset, size);
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
        return "pages/admin/user/modify";
    }

    @PostMapping("/user/update")
    public String UpdateUser(
            @ModelAttribute User user
    ) {
        User existUser = userService.findUserByUserId(user.getUserId());
        if (existUser == null) {
            return "redirect:/admin/user/list";
        }
        try {
            userService.UpdateUserForAdmin(user);
            return "redirect:/admin/user/list";
        } catch (Exception e) {
            return "redirect:/admin/user/list";
        }

    }

    @GetMapping("/moimList/delete")
    public String deleteMoimList(
            @RequestParam("moimId") int moimId,
            Model model
    ) {
        //update로 스테이터스 N으로 변경
        int result = moimService.deleteMoimList(moimId);
        if (result > 0) {
            model.addAttribute("msg", "모임이 삭제 되었습니다.");
            model.addAttribute("url", "/pages/admin/moimList/moimList");
            return "pages/sendRedirect";
        } else {
            throw new MoimException("모임 삭제를 실패하였습니다.");
        }
    }

    @GetMapping("/moim/list")
    public String MoimList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        int offset = (page - 1) * size;

        int totalCount = moimService.countMoimForAdmin();
        ArrayList<Moim> moimList = moimService.findManyMoimForAdmin(offset, size);

        model.addAttribute("moimList", moimList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("baseUrl", "/admin/moim/list");

        return "pages/admin/moim/list";
    }
}
