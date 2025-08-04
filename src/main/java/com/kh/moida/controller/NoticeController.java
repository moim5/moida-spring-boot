package com.kh.moida.controller;

import com.kh.moida.model.Notice;
import com.kh.moida.service.NoticeService;
import com.kh.moida.common.pagination.PageInfo;
import com.kh.moida.common.pagination.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController {
    public final NoticeService noticeService;

    @GetMapping("list")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            Model model,
            HttpServletRequest request
    ) {
        int listCount = noticeService.getListCount();
//        System.out.println("listCount = " + listCount);

        PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5); // 현재페이지, 몇개가있는지, 몇개씩 보여질지

        ArrayList<Notice> list = noticeService.selectBoardList(pi);

//        System.out.println("list = " + list);

        model.addAttribute("list", list);
        model.addAttribute("pi", pi);
        model.addAttribute("loc", request.getRequestURI());

        return "pages/notice/list";
    }

    @GetMapping("write")
    public String write() {
        return "pages/notice/write";
    }

    @PostMapping("insert")
    public String insert(
            @ModelAttribute Notice notice,
            MultipartFile NoticeImage
    ) throws IOException {
        noticeService.insertNoticeFile(notice, NoticeImage);
        //int result = noticeService.write(notice);
        return "redirect:/notice/list";
    }

    @GetMapping("/{noticeId}/{page}")
    public String noticeBoard(
            @PathVariable("noticeId") int noticeId,
            @PathVariable("page") int page,
            Model model
    ) {
        Notice n = noticeService.selectBoard(noticeId);
        noticeService.updateCount(n);

        model.addAttribute("n", n).addAttribute("page", page);
        return "pages/notice/detail";
    }

    @PostMapping("updForm")
    public String updateForm(
            @ModelAttribute Notice notice,
            @RequestParam("page") int page,
            Model model
    ) {
        Notice n = noticeService.updateForm(notice);
        model.addAttribute("n", n).addAttribute("page", page);

        return "pages/notice/edit";
    }

    @PostMapping("update")
    public String updateBoard(
            @ModelAttribute Notice notice,
            @RequestParam("noticeImage") MultipartFile noticeImage,
            @RequestParam("page") int page,
            Model model
    ) throws IOException {
        noticeService.updateBoard(notice, noticeImage);

        model.addAttribute("n", notice);
        model.addAttribute("page", page);
        return "redirect:/notice/" + notice.getNoticeId() + "/" + page;
    }

    @PostMapping("delete")
    public String noticeDelete(@RequestParam("noticeId") int noticeId) {
        noticeService.deleteFile(noticeId);
        return "redirect:/notice/list";
    }
}
