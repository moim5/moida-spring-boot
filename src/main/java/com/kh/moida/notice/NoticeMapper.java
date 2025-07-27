package com.kh.moida.notice;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    int getListCount();

    ArrayList<Notice> selectBoardList(PageInfo pi);

    int write(Notice notice);

    Notice selectBoard(int id);

    int delete(int id);

    Notice updateForm(Notice notice);

    int updateBoard(Notice notice);

    int updateCount(Notice notice);

    void insertNotice(Notice notice);
}
