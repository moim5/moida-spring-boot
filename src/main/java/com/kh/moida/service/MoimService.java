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

	//모임 아이디찾기
    public Moim findById(int moimId) {
        return moimMapper.findMoimById(moimId);
    }

	public void moimUpdate(Moim moim, MultipartFile moimImage, User user) {
		// 들어오는 값은 있을 것이다. 근데 파일이 변경됐을까?
		if (moimImage == null || moimImage.isEmpty()) { 
            moimMapper.updateMoimWithoutFile(moim); //사진 업데이트 없을때 
            return;
        }

        if (moim.getFileConvert() != null) { // 사진 업데이트 있을때 기존 사진 삭제하기 moim에 등록된 바뀐파일명
            fileUploadService.deleteFile(moim.getFileConvert()); // 삭제
        }

        String originalName = moimImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "category/" + timestamp + "." + ext;

        try {
			fileUploadService.uploadFile(moimImage, newName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 파일업로드 

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file); //파일 업로드 (원본명, 새이름)

        moim.setFileOrigin(originalName);
        moim.setFileConvert(newName);
        moim.setFileId(file.getFileId());

        moimMapper.updateMoimWithFile(moim);
		
        // 1. 새로운 파일이 생겼나? -> 없으면, 디비만 쓰고 끝
        // 2. 그럼 원래 있던 파일을 지우자
        // 3. 그리고 새로운 파일을 올리자
        // 4. 디비 정보를 업데이트 하자
	}
		
    //admin:모임삭제
	public int deleteMoimList(int moimId) {
		return moimMapper.deleteMoimList(moimId);
	}
	
	//해당하는 모임에 대한 리뷰 리스트 출력
	//리뷰 이미 작성했을 경우 write view이동 막기->모임아이디 기준으로 찾아야돼서 맵퍼 다르게하기
	 public int countReview(int moimId) {
		 return moimMapper.countReview(moimId);
	  }


	 public void moimJoinMoim(Moim moim, User user) {
		// TODO Auto-generated method stub
		// 가입 하려는 모임 ID가 뭔지 참여하기 버튼 누르고, 그 모임의 moimId 가져오기
		// user 에서 user_id 가져오기 
		 
		 moimMapper.moimJoinMoim(moim, user);
		 return;
		
	 }

    public int countHostedMoim(Long userId) {
        return moimMapper.countHostedMoimCount(userId);
    }

    public List<Moim> findManyHostedMoim(Long userId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findHostedMoim(params);

    }

    public int countJoinedMoim(Long userId) {
        return moimMapper.countJoinedMoim(userId);
    }

    public List<Moim> findManyJoinedMoim(Long userId, int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findManyJoinedMoim(params);

    }

	public void joinMoimCancel(Moim moim, User user) {
		// 참여 신청 취소하고 싶은 moim_id 
		 moimMapper.joinMoimCancel(moim,user);
		
	}
}
