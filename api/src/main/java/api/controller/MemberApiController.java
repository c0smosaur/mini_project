package api.controller;

import api.common.result.ResultWrapper;
import api.config.jwt.JwtProvider;
import api.model.response.MemberResponse;
import api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/my-page")
    public ResultWrapper<MemberResponse> memberInfo(){
        MemberResponse response = memberService.info();
        return ResultWrapper.OK(response);
    }

    @PostMapping("/logout")
    public ResultWrapper logout(){
        memberService.memberLogout();
        return ResultWrapper.OK(null);
    }
}
