package com.kh.moida.notice;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    int getListCount();

    ArrayList<Notice> selectBoardList(PageInfo pi);

    int write(Notice notice);

    Notice selectBoard(int id);

    int delete(int id);
}
