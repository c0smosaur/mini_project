package api.controller;

import api.common.result.ResultWrapper;
import api.config.jwt.JwtProvider;
import api.model.request.MemberLoginRequest;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResultWrapper<MemberResponse> register(
            @RequestBody MemberRegisterRequest request) {
        MemberResponse response = memberService.register(request);
        return ResultWrapper.OK(response);
    }

    @PostMapping("/login")
    public ResultWrapper<MemberLoginResponse> signIn(
            @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberService.signIn(request);
        return ResultWrapper.OK(response);
    }

    @GetMapping("/my-page")
    public ResultWrapper<MemberResponse> memberInfo(){
        MemberResponse response = memberService.info();
        return ResultWrapper.OK(response);
    }

    @PostMapping("/logout")
    public ResultWrapper<Void> logout(){
        memberService.memberLogout();
        return ResultWrapper.OK(null);
    }
}
