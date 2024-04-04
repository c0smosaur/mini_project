package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.MemberLoginRequest;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<ResultWrapper<MemberResponse>> register(
            @RequestBody MemberRegisterRequest request) {
        MemberResponse response = memberService.register(request);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ResultWrapper<MemberLoginResponse>> signIn(
            @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberService.signIn(request);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(response));
    }

    @GetMapping("/my-page")
    public ResponseEntity<ResultWrapper<MemberResponse>> memberInfo(){
        MemberResponse response = memberService.info();
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResultWrapper<Void>> logout(){
        memberService.memberLogout();
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(null));
    }
}
