package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.config.oauth2.OAuth2UserInfo;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import db.entity.MemberEntity;
import db.entity.SocialOAuth;
import db.enums.MemberStatus;
import db.enums.MemberType;
import db.enums.OAuthProvider;

import java.util.Optional;

@Converter
public class MemberConverter {

    // 회원가입
    public MemberEntity toEntity(MemberRegisterRequest request) {
        return Optional.ofNullable(request)
                .map(it -> {
                    return MemberEntity.builder()
                            .username(it.getUsername())
                            .password(it.getPassword())
                            .name(it.getName())
                            .status(MemberStatus.REGISTERED)
                            .type(MemberType.USER)
                            .profileImage(it.getProfileImage())
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.BAD_REQUEST));
    }

    public MemberEntity toEntity(OAuthProvider oAuthProvider, OAuth2UserInfo oAuth2UserInfo){
        return MemberEntity.builder()
                            .username(oAuth2UserInfo.getEmail())
                            .name(oAuth2UserInfo.getName())
                            .status(MemberStatus.REGISTERED)
                            .type(MemberType.USER)
                            .socialOAuth(SocialOAuth.builder()
                                    .oAuthId(oAuth2UserInfo.getOAuth2Id())
                                    .oAuthProvider(oAuthProvider)
                                    .build())
                            .build();
    }

    // 로그인 반환값 (유저 정보와 토큰 2개)
    public MemberLoginResponse toLoginResponse(MemberEntity entity,
                                               String accessToken,
                                               String refreshToken) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    return MemberLoginResponse.builder()
                            .memberId(it.getId())
                            .username(it.getUsername())
                            .type(it.getType())
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.BAD_REQUEST));
    }

    // 유저 정보 조회 시 반환
    public MemberResponse toResponse(MemberEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    return MemberResponse.builder()
                            .memberId(it.getId())
                            .name(it.getName())
                            .username(it.getUsername())
                            .type(it.getType())
                            .profileImage(it.getProfileImage())
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.BAD_REQUEST));
    }
}
