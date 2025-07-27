package com.kh.moida.notice;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.model.Category;
import com.kh.moida.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper mapper;
    private final FileUploadService fileUploadService;
    private final FileMapper fileMapper;

    public int getListCount() {
        return mapper.getListCount();
    }

    public ArrayList<Notice> selectBoardList(PageInfo pi) {
        return mapper.selectBoardList(pi);
    }

    public int write(Notice notice) {
        return mapper.write(notice);
    }

    public Notice selectBoard(int id) {
        return mapper.selectBoard(id);
    }

    public int delete(int id) {
        return mapper.delete(id);
    }

    public Notice updateForm(Notice notice) {
        return mapper.updateForm(notice);
    }

    public int updateBoard(Notice notice) {
        return mapper.updateBoard(notice);
    }

    public int updateCount(Notice notice) {
        return mapper.updateCount(notice);
    }

    public void insertNoticeFile(Notice notice, MultipartFile NoticeImage) throws IOException {
        String originalName = NoticeImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "notice/" + timestamp + "." + ext;

        fileUploadService.uploadFile(NoticeImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        notice.setFileId(file.getFileId());
        mapper.insertNotice(notice);
    }
}
