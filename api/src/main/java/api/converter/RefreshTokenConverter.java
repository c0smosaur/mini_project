package api.converter;

import api.common.annotation.Converter;
import api.config.jwt.TokenDto;
import db.entity.MemberEntity;
import db.entity.RefreshTokenEntity;

@Converter
public class RefreshTokenConverter {

    public RefreshTokenEntity toEntity(MemberEntity entity, TokenDto token) {
        return RefreshTokenEntity.builder()
                .memberId(entity.getId())
                .token(token.getToken())
                .expiredAt(token.getExpiredAt())
                .issueCount(0)
                .build();
    }
}
