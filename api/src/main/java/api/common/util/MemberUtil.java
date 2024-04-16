package api.common.util;


import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepository;

    public Long getCurrentMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        return memberEntity.getId();
    }
}
