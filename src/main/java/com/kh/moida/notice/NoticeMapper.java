package com.kh.moida.notice;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    int getListCount();

    ArrayList<Notice> selectBoardList(PageInfo pi);
}
