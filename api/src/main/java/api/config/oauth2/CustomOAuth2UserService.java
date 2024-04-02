package api.config.oauth2;

import api.common.error.GeneralErrorCode;
import api.common.error.MemberErrorCode;
import api.common.exception.ResultException;
import api.converter.MemberConverter;
import api.model.response.MemberResponse;
import db.entity.MemberEntity;
import db.entity.SocialOAuth;
import db.enums.MemberStatus;
import db.enums.OAuthProvider;
import db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.file.attribute.UserPrincipal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
                new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // 로그인 플랫폼 구분
        OAuthProvider oAuthProvider = OAuthProvider.valueOf(userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase());

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoMaker.getOAuth2UserInfo(
                oAuthProvider,
                oAuth2User.getAttributes());

        // username(=email)이 null이면 예외처리
        if (StringUtils.hasText(oAuth2UserInfo.getEmail())){
            throw new ResultException(GeneralErrorCode.NOT_FOUND);
        }
        // 일치하는 email 가진 member 찾아서 반환
        Optional<MemberEntity> memberEntity = memberRepository.findFirstByUsernameAndStatus(
                oAuth2UserInfo.getEmail(),
                MemberStatus.REGISTERED);

        // 가입된 경우
        if (memberEntity.isPresent()){
            MemberEntity member = memberEntity.get();
            // 이미 다른 방식으로 가입된 이메일일 경우 예외처리
            if (!memberEntity.get().getSocialOAuth().getOAuthProvider().equals(oAuthProvider)){
                throw new ResultException(MemberErrorCode.DUPLICATE_USERNAME);
            }
            member = updateMember(member, oAuthProvider, oAuth2UserInfo);
        }

        // 미가입인 경우
        else {
            var member = registerUser(oAuthProvider, oAuth2UserInfo);
        }
        return OAuthPrincipal.create((Map<String, Object>) oAuth2UserInfo);
    }

    private MemberEntity registerUser(OAuthProvider oAuthProvider, OAuth2UserInfo oAuth2UserInfo) {

        MemberEntity memberEntity = memberConverter.toEntity(oAuthProvider, oAuth2UserInfo);
        return memberRepository.save(memberEntity);
    }

    private MemberEntity updateMember(MemberEntity member,
                                      OAuthProvider oAuthProvider,
                                      OAuth2UserInfo oAuth2UserInfo){
        SocialOAuth socialOAuth = member.getSocialOAuth();
        socialOAuth.setOAuthProvider(oAuthProvider);
        socialOAuth.setOAuthId(oAuth2UserInfo.getOAuth2Id());
        member.setSocialOAuth(socialOAuth);
        return memberRepository.save(member);
    }
}
