package com.kh.moida.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.kh.moida.dto.MoimAttendeeWithUser;
import com.kh.moida.dto.ReviewWithUser;
import com.kh.moida.mapper.ReviewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.mapper.MoimAttendeeMapper;
import com.kh.moida.mapper.MoimMapper;
import com.kh.moida.model.File;
import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import com.kh.moida.model.Question;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MoimService {
    private final MoimMapper moimMapper;
    private final FileMapper fileMapper;
    private final FileUploadService fileUploadService;
    private final MoimAttendeeMapper maMapper;
    private final MoimAttendeeMapper moimAttendeeMapper;
    private final ReviewMapper reviewMapper;

    public Moim insertMoim(User loginUser, Moim moim, MultipartFile moimImage) throws IOException {
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
        return moim;
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
    public Moim findById(Long moimId) {
        return moimMapper.findMoimById(moimId);
    }

    public void moimUpdate(Moim moim, MultipartFile moimImage) throws IOException {
        if (moimImage == null || moimImage.isEmpty()) {
            moimMapper.updateMoimWithoutFile(moim);
            return;
        }

        if (moim.getFileConvert() != null) {
            fileUploadService.deleteFile(moim.getFileConvert());
        }

        String originalName = moimImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "moim/" + timestamp + "." + ext;

        fileUploadService.uploadFile(moimImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        moim.setFileOrigin(originalName);
        moim.setFileConvert(newName);
        moim.setFileId(file.getFileId());

        moimMapper.updateMoimWithFile(moim);
    }

    //admin:모임삭제
    public int deleteMoimList(int moimId) {
        return moimMapper.deleteMoimList(moimId);
    }

    public int moimJoinMoim(User user, int moimId) {
        // 가입 하려는 모임 ID가 뭔지 참여하기 버튼 누르고, 그 모임의 moimId 가져오기
        // user 에서 user_id 가져오기
//    	moimId.getUserId();
//        moimMapper.moimJoinMoim(user, moimId);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("moimId", moimId);
        int count = maMapper.searchMoimAttendee(params);
        if (count > 0) {
            return 0;
        }

        return maMapper.joinMoim(params);

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

    public int cancelMoimAttendee(int moimId, User user) {
        // 참여 신청 취소하고 싶은 moim_id
        Map<String, Object> params = new HashMap<>();
        params.put("moimId", moimId);
        params.put("userId", user.getUserId());
        int count = maMapper.searchMoimAttendee(params);
        if (count == 0) {
            return 0;
        }
        return maMapper.joinMoimCancel(params);
    }

    public int moimquestion(Question question) {
        return moimMapper.moimquestion(question);
    }

    public ArrayList<Question> findQuestion(Long moimId) {
        return moimMapper.findQuestion(moimId);
    }

    public int moimanswer(Question question) {
        return moimMapper.moimanswer(question);
    }

    public int cancelMoim(Long moimId, User user) {
        Moim moim = moimMapper.findMoimById(moimId);
        if (moim.getUserId() != user.getUserId()) {
            return 0;
        }
        return moimMapper.cancelMoim(moimId);
    }

    //review 리스트 뽑기 (moimDetail)
    public ArrayList<ReviewWithUser> getReviewList(Long moimId) {
        return reviewMapper.getReviewList(moimId);
    }


    public int reviveMoim(Long moimId, User user) {
        Moim moim = moimMapper.findMoimById(moimId);
        if (moim.getUserId() != user.getUserId()) {
            //모임 호스트 아이디랑 로그인유저 아이디랑 같지 않으면
            return 0;
        }
        return moimMapper.reviveMoim(moimId);
    }

    public List<MoimAttendeeWithUser> attendeeList(Long moimId, User loginUser) {
        Moim moim = moimMapper.findMoimById(moimId);
        if (!Objects.equals(moim.getUserId(), loginUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return moimAttendeeMapper.findMoimAttendee(moimId);
    }

    public int isMoimAttendee(Long moimId, Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("moimId", moimId);
        params.put("userId", userId);
        return moimAttendeeMapper.searchMoimAttendee(params);
    }

    public ArrayList<Question> findMyQuestion(Long userId) {
        return moimMapper.findMyQuestion(userId);
    }

    public int countMoimForAdmin() {
        return moimMapper.countMoimForAdmin();
    }

    public ArrayList<Moim> findManyMoimForAdmin(int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return moimMapper.findManyMoimForAdmin(params);

    }

    //평균 별점 구하기
    public double getRateAvgByMoimId(Long moimId) {
        Double avg = reviewMapper.getRateAvgByMoimId(moimId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public int questionDelete(int quesId) {
        return moimMapper.questionDelete(quesId);
    }

    public int answerDelete(int quesId) {
        return moimMapper.answerDelete(quesId);
    }
}
