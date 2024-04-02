package api.config.oauth2;

import db.enums.OAuthProvider;

import java.util.Map;

public class OAuth2UserInfoMaker {

    public static OAuth2UserInfo getOAuth2UserInfo(OAuthProvider oAuthProvider,
                                                   Map<String, Object> attributes){
        // 현재는 google만... 추후 naver, kakao 등 추가
        switch (oAuthProvider){
            case GOOGLE -> {
                return new GoogleOAuth2User(attributes);
            }
            default -> throw new IllegalArgumentException("OAuthProvider 타입 불일치");
        }
    }

}
