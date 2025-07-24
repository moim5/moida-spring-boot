package com.kh.moida.service;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.model.File;
import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import org.springframework.stereotype.Service;

import com.kh.moida.mapper.MoimMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MoimService {
    private final MoimMapper moimMapper;
    private final FileMapper fileMapper;
    private final FileUploadService fileUploadService;

    public void insertMoim(User loginUser, Moim moim, MultipartFile moimImage) throws IOException {
        // Start 이름 바꾸기
        String originalName = moimImage.getOriginalFilename(); // 들어온 파일의 이름을 구하는 줄
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1); // 파일의 확장자를 구하는 줄
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")); // 시간으로 String을 구하는 줄
        String newName = "moim/" + timestamp + "." + ext; // moim/타임스탬프.확장자
        // End 이름 바꾸기

        fileUploadService.uploadFile(moimImage, newName); // 파일 업로드

        // Start 파일에 대한 DB 삽입
        // Start 테이블에 넣기 전, File 이라는 객체를 만들어줌
        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        // End 테이블에 넣기 전, File 이라는 객체를 만들어줌


        fileMapper.insertFile(file); // 실제 File에 대한 DB 삽입
        // End 파일에 대한 DB 삽입

        // Service에 들어와서 File을 먼저 작성을 함
        // 이후 Moim에 대해서 작성을 해야해요.

        moim.setUserId(loginUser.getUserId());
        moim.setFileId(file.getFileId());
        moim.setFileOrigin(originalName); // 그냥 넣었다
        moim.setFileConvert(newName); // 그냥 넣었다
        moim.setIsVisible("Y");
        moim.setIsActive("Y");
        moimMapper.insertMoim(moim);
    }

	public List<Moim> findMoim(Long categoryId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findMoims(params);
	}

	public int countMoim(Long categoryId) {
		return moimMapper.countMoim(categoryId);
	}

    public Moim findById(int moimId) {
        return moimMapper.findMoimById(moimId);
    }
}
