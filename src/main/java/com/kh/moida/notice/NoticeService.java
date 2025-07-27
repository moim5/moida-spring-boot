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

    public int deleteFile(int id) {
        // 1. 조회
        Notice notice = mapper.findNoticeWithFile(id);
        if (notice == null) {
            throw new IllegalArgumentException("해당 공지사항 또는 파일이 존재하지 않습니다. id=" + id);
        }

        // 2. S3 삭제
        String s3Key = notice.getFileConvert();
        if (s3Key != null && !s3Key.isEmpty()) {
            fileUploadService.deleteFile(s3Key);
        }

        // 3. 공지사항 삭제
        int result = mapper.delete(id);

        // 4. 파일 삭제
        fileMapper.deleteFile(notice.getFileId());

        return result;
    }

    public Notice updateForm(Notice notice) {
        return mapper.updateForm(notice);
    }

    public int updateBoard(Notice notice, MultipartFile noticeImage) throws IOException {

        // 새 파일이 업로드 되었는지 체크
        if (noticeImage != null && !noticeImage.isEmpty()) {
            // 기존 파일이 있으면 삭제
            if (notice.getFileConvert() != null && !notice.getFileConvert().isEmpty()) {
                fileUploadService.deleteFile(notice.getFileConvert());
            }

            String originalName = noticeImage.getOriginalFilename();
            String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String newName = "category/" + timestamp + "." + ext;

            // 새 파일 업로드
            fileUploadService.uploadFile(noticeImage, newName);

            // 파일 DB 저장
            File file = new File();
            file.setFileOrigin(originalName);
            file.setFileConvert(newName);
            fileMapper.insertFile(file);

            // 공지사항에 파일 정보 세팅
            notice.setFileOrigin(originalName);
            notice.setFileConvert(newName);
            notice.setFileId(file.getFileId());
        } else {
            // 새 파일 없으면 기존 파일 정보 유지
            // (필요 시 추가 로직 가능)
        }

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
