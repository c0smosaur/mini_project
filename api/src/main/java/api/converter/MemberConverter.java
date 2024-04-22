package api.converter;

import api.common.annotation.Converter;
import api.config.jwt.BothTokensDto;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.enums.MemberType;

@Converter
public class MemberConverter {

    // 회원가입
    public MemberEntity toEntity(MemberRegisterRequest request) {
        return MemberEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .name(request.getName())
                .status(MemberStatus.REGISTERED)
                .type(MemberType.USER)
                .profileImage(request.getProfileImage())
                .build();
    }

    // 로그인 반환값 (유저 정보와 토큰 2개)
    public MemberLoginResponse toLoginResponse(MemberEntity entity,
                                               BothTokensDto bothTokens) {
        return MemberLoginResponse.builder()
                .memberId(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .type(entity.getType())
                .accessToken(bothTokens.getAccessToken().getToken())
                .refreshToken(bothTokens.getRefreshToken().getToken())
                .build();
    }

    // 유저 정보 조회 시 반환
    public MemberResponse toResponse(MemberEntity entity) {
        return MemberResponse.builder()
                .memberId(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .type(entity.getType())
                .profileImage(entity.getProfileImage())
                .build();
    }
}
