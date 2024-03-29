package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.MemberLoginRequest;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberOpenApiController {

    private final MemberService memberService;

    // TODO 이미지 저장 처리
    @PostMapping("/register")
    public ResultWrapper<MemberResponse> register(
            @RequestBody MemberRegisterRequest request) {
        MemberResponse response = memberService.register(request);
        return ResultWrapper.OK(response);
    }

    @PostMapping("/sign-in")
    public ResultWrapper<MemberLoginResponse> signIn(
            @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberService.signIn(request);
        return ResultWrapper.OK(response);
    }

}
