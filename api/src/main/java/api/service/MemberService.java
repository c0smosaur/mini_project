package api.service;

import api.common.error.GeneralErrorCode;
import api.common.error.MemberErrorCode;
import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.config.jwt.BothTokensDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberUtil memberUtil;

    @Transactional
    public MemberResponse register(MemberRegisterRequest request){
        // 중복 이메일 확인
        if(memberRepository.findFirstByUsernameAndStatus(
                request.getUsername(), MemberStatus.REGISTERED).isPresent()){
            throw new ResultException(MemberErrorCode.DUPLICATE_USERNAME);
        }

        // 요청에서 받은 비밀번호 암호화하여 저장
        MemberEntity entity = memberConverter.toEntity(request);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        MemberEntity newEntity = memberRepository.save(entity);
        return memberConverter.toResponse(newEntity);
    }

    @Transactional
    public MemberLoginResponse login(MemberLoginRequest request) {
        // username이 일치하고 등록된 상태인 회원을 찾음
        MemberEntity member = memberRepository.findFirstByUsernameAndStatus(
                        request.getUsername(), MemberStatus.REGISTERED
                // 요청으로 받은 password를 passwordEncoder로 암호화한 결과가 db에 저장된 암호화된 password와 일치하는지 확인
                ).filter(entity -> passwordEncoder.matches(
                        request.getPassword(), entity.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        // token 발행 - refreshToken에는 만료시간만
        TokenDto refreshToken = jwtProvider.generateRefreshToken();

        // 남아있을지도 모르는 refresh token 삭제
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findFirstByMemberId(member.getId());
        refreshTokenEntity.ifPresent(refreshTokenRepository::delete);

        // 새 refreshToken DB에 저장
        RefreshTokenEntity newRefreshTokenEntity = refreshTokenConverter.toEntity(
                member, refreshToken);
        refreshTokenRepository.save(newRefreshTokenEntity);

        TokenDto accessToken = jwtProvider.generateAccessToken(member);

        BothTokensDto bothTokens = jwtProvider.getBothTokens(accessToken, refreshToken);

        return memberConverter.toLoginResponse(
                member, bothTokens);
    }

    @Transactional(readOnly = true)
    public MemberResponse info(){
        MemberEntity memberEntity = memberRepository.findFirstByIdAndStatus(
                memberUtil.getCurrentMember(), MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        return memberConverter.toResponse(memberEntity);
    }

    @Transactional
    public void memberLogout(){
        MemberEntity memberEntity = memberRepository.findFirstByIdAndStatus(
                memberUtil.getCurrentMember(), MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        // 멤버에게 발급된 refresh token db에서 삭제
        jwtProvider.invalidateRefreshToken(memberEntity.getId());

        // 현재 로그인한 멤버 정보 지움
        SecurityContextHolder.clearContext();
    }

}
