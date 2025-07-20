package com.kh.moida.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper mapper;

    public int getListCount() {
        return mapper.getListCount();
    }

    public ArrayList<Notice> selectBoardList(PageInfo pi) {
        return mapper.selectBoardList(pi);
    }

    public int write(Notice notice) {
        return mapper.write(notice);
    }
}
