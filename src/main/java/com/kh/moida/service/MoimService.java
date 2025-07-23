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
        String originalName = moimImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "moim/" + timestamp + "." + ext;

        fileUploadService.uploadFile(moimImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        moim.setUserId(loginUser.getUserId());
        moim.setFileId(file.getFileId());
        moim.setFileOrigin(originalName);
        moim.setFileConvert(newName);
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
