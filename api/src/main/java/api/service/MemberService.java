package api.service;

import api.common.error.GeneralErrorCode;
import api.common.error.MemberErrorCode;
import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.JwtProvider;
import api.config.jwt.TokenDto;
import api.converter.MemberConverter;
import api.converter.RefreshTokenConverter;
import api.model.request.MemberLoginRequest;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import db.entity.MemberEntity;
import db.entity.RefreshTokenEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import db.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenConverter refreshTokenConverter;

    public MemberResponse register(MemberRegisterRequest request) {
        // 요청에서 받은 비밀번호 암호화하여 저장
        if(memberRepository.findFirstByUsernameAndStatus(
                request.getUsername(),
                MemberStatus.REGISTERED).isPresent()){
            throw new ResultException(MemberErrorCode.DUPLICATE_USERNAME);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        MemberEntity entity = memberConverter.toEntity(request);
        MemberEntity newEntity = memberRepository.save(entity);
        MemberResponse response = memberConverter.toResponse(newEntity);
        return response;
    }

    public MemberLoginResponse signIn(MemberLoginRequest request) {
        // username이 일치하고 등록된 상태인 회원을 찾음
        MemberEntity member = memberRepository.findFirstByUsernameAndStatus(
                        request.getUsername(),
                        MemberStatus.REGISTERED
                // 요청으로 받은 password를 passwordEncoder로 암호화한 결과가 db에 저장된 암호화된 password와 일치하는지 확인
                ).filter(it -> passwordEncoder.matches(
                        request.getPassword(),
                        it.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        // token 발행 - refreshToken에는 만료시간만
        TokenDto refreshToken = jwtProvider.generateRefreshToken();

        // 남아있을지도 모르는 refresh token 삭제
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findFirstByMemberId(member.getId());
        refreshTokenEntity.ifPresent(refreshTokenRepository::delete);

        // 새 refreshToken DB에 저장
        RefreshTokenEntity newRefreshTokenEntity = refreshTokenConverter.toEntity(
                member,
                refreshToken);
        refreshTokenRepository.save(newRefreshTokenEntity);

        // token 발행 - accessToken에는 유저정보 포함
        Map<String, Object> data = new HashMap<>();
        data.put("id", member.getId());
        data.put("name",member.getName());
        data.put("username",member.getUsername());
        data.put("type",member.getType());

        TokenDto accessToken = jwtProvider.generateAccessToken(data);

        MemberLoginResponse response = memberConverter.toLoginResponse(
                member,
                accessToken.getToken(),
                refreshToken.getToken());
        return response;
    }

    public MemberResponse info(){
        // 현재 로그인한 멤버 정보 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        return memberConverter.toResponse(memberEntity);
    }

    public void memberLogout(){
        // 현재 로그인한 멤버 정보 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        // 멤버에게 발급된 refresh token db에서 삭제
        jwtProvider.invalidateRefreshToken(memberEntity.getId());

        // 현재 로그인한 멤버 정보 지움
        SecurityContextHolder.clearContext();
    }

}
