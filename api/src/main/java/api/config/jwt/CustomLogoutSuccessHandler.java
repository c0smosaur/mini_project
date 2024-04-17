package api.config.jwt;

import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import db.entity.MemberEntity;
import db.entity.RefreshTokenEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import db.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        memberLogout(request);
    }

    private void memberLogout(HttpServletRequest request){
        MemberEntity memberEntity = memberRepository.findFirstByIdAndStatus(
                memberUtil.getCurrentMember(), MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository
                .findFirstByMemberId(memberEntity.getId());

        refreshTokenEntity.ifPresent(refreshTokenRepository::delete);
    }
}
