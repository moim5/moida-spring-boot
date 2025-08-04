package com.kh.moida.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kh.moida.dto.ReviewWithUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.kh.moida.dto.MoimAttendeeWithUser;
import com.kh.moida.model.Moim;
import com.kh.moida.model.User;
import com.kh.moida.model.UserPrincipal;
import com.kh.moida.model.Answer;
import com.kh.moida.model.Question;
import com.kh.moida.service.MoimService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/moim")
@RequiredArgsConstructor
public class MoimController {
    private final MoimService moimService;

    @GetMapping("/create")
    public String MoimCreate() {
        return "pages/moim/create";
    }

    @PostMapping("/insert")
    public String MoimWrite(
            @ModelAttribute Moim moim,
            @RequestParam("moimImage") MultipartFile moimImage,
            @AuthenticationPrincipal UserPrincipal loginUser,
            Model model
    ) {
        try {
            Moim createdMoim = moimService.insertMoim(loginUser.getUser(), moim, moimImage);
            return "redirect:/moim/" + createdMoim.getMoimId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "pages/moim/create";
        }
    }

    //moim_detail이동
    @GetMapping("/{moimId}")
    public String MoimDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("moimId") Long moimId,
            Model model
    ) {
        Moim moim = moimService.findById(moimId);
        User loginUser = null;
        int isMoimAttendee = 0;
        if (userPrincipal != null) {
            loginUser = userPrincipal.getUser();
            isMoimAttendee = moimService.isMoimAttendee(moimId, userPrincipal.getUser().getUserId());

        }
        ArrayList<Question> questions = moimService.findQuestion(moim.getMoimId());
        ArrayList<ReviewWithUser> reviewList = moimService.getReviewList(moimId);

        //모임에 대한 평균 별점 추가
        double avgRating = moimService.getRateAvgByMoimId(moimId);
        
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("moim", moim)
             .addAttribute("questions", questions)
             .addAttribute("loginUser", loginUser)
             .addAttribute("isMoimAttendee", isMoimAttendee)
             .addAttribute("avgRating", avgRating);
        return "pages/moim/detail";
    }
    

    


    private void addAttribute(String string, double avgRating) {
		// TODO Auto-generated method stub
		
	}

	@GetMapping("/modify/{moimId}")
    public String MoimModify(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @PathVariable("moimId") Long moimId,
            Model model
    ) {
        Moim moim = moimService.findById(moimId);
        if (moim == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(moim.getUserId(), loginUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        model.addAttribute("moim", moim);
        return "pages/moim/modify";
    }

    @PostMapping("/update")
    public String MoimUpdate(
            @ModelAttribute Moim moim,
            @RequestParam("moimImage") MultipartFile moimImage,
            @AuthenticationPrincipal(expression = "user") User loginUser,
            Model model
    ) {
        Moim prevMoim = moimService.findById(moim.getMoimId());
        if (prevMoim == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(prevMoim.getUserId(), loginUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        try {
            moimService.moimUpdate(moim, moimImage);
            return "redirect:/my/moim/hosted/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/moim/modify/" + moim.getMoimId();
        }
    }

    // 모임 중단
    @PostMapping("/cancelMoim/{moimId}")
    @ResponseBody
    public String CancelMoim(
            @PathVariable("moimId") Long moimId,
            @AuthenticationPrincipal UserPrincipal loginUser
    ) {
        int result = moimService.cancelMoim(moimId, loginUser.getUser());
        if (result > 0) {
            return "true";
        }
        return "false";
    }

    // 모임 중단 취소 ( 재 활성화 ? )
    @PostMapping("/reviveMoim/{moimId}")
    @ResponseBody
    public String ReviveMoim(
            @PathVariable("moimId") Long moimId,
            @AuthenticationPrincipal UserPrincipal loginUser
    ) {
        int result = moimService.reviveMoim(moimId, loginUser.getUser());
        if (result > 0) {
            return "true";
        }
        return "false";
    }


    @PostMapping("/join/{moimId}") //모임 참여
    @ResponseBody
    public String CreateMoimAttendee(
            @AuthenticationPrincipal UserPrincipal loginUser,
            @PathVariable("moimId") int moimId
    ) {
        int result = moimService.moimJoinMoim(loginUser.getUser(), moimId);
        if (result > 0) {
            return "true";
        }
        return "false";
    }

    //모임 참가신청 취소
    //참가 신청할 모임 id 
    @PostMapping("/cancel/{moimId}")
    @ResponseBody
    public String CancelMoimAttendee(
            @AuthenticationPrincipal UserPrincipal loginUser,
            @PathVariable("moimId") int moimId
    ) {
        int result = moimService.cancelMoimAttendee(moimId, loginUser.getUser());
        if (result > 0) {
            return "true";
        }
        return "false";
    }

    // 문의 등록
    @PostMapping("question")
    public String moimquestion(@ModelAttribute Question question, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User loginUser = userPrincipal.getUser();
        question.setQuestionUserId(loginUser.getUserId());
        int result = moimService.moimquestion(question);
        return "redirect:/moim/" + question.getMoimId();
    }

    @PostMapping("answer")
    public String moimanswer(@ModelAttribute Question question, @ModelAttribute Answer answer, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int result = moimService.moimanswer(question);
        return "redirect:/moim/" + question.getMoimId();
    }

    @PostMapping("/attendee/list")
    @ResponseBody
    public List<MoimAttendeeWithUser> attendeeList(
            @AuthenticationPrincipal(expression = "user") User loginUser,
            @RequestParam("moimId") Long moimId
    ) {
        return moimService.attendeeList(moimId, loginUser);
    }

    @PostMapping("question/delete")
    public String questionDelete(@RequestParam("quesId") int quesId,
                               @RequestParam("moimId") Long moimId) {
        moimService.answerDelete(quesId);
        moimService.questionDelete(quesId);

        // 삭제 후 현재 상세 페이지로 리다이렉트
        return "redirect:/moim/" + moimId;

    }
}
