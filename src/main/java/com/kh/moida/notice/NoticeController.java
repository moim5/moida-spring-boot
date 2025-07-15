package com.kh.moida.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController {

    @GetMapping("list")
    public String list() {
        System.out.println("true = " + true);
        return "pages/notice/list";
    }

}
