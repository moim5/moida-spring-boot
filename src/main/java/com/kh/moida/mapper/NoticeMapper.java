package com.kh.moida.mapper;

import com.kh.moida.model.Notice;
import com.kh.moida.common.pagination.PageInfo;
import org.apache.ibatis.annotations.Mapper;

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

    Notice findNoticeWithFile(int id);
}
