package api.config.jwt;

import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.entity.MemberEntity;
import db.entity.RefreshTokenEntity;
import db.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${token.accessToken.plus-hour}")
    private Long ACCESS_TOKEN_PLUS_HOUR;
    @Value("${token.accessToken.plus-minute}")
    private Long ACCESS_TOKEN_PLUS_MINUTE;
    @Value("${token.refreshToken.plus-hour}")
    private Long REFRESH_TOKEN_PLUS_HOUR;
    // TODO: PostConstruct도 고려
    @Value("${token.secret.tokenKey}")
    private String SECRET_KEY;

    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    // Access Token 발행 메서드
    public TokenDto generateAccessToken(MemberEntity member) {
        String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());

        Map<String, Object> claim = new HashMap<>();
        claim.put("username",member.getUsername());
        claim.put("type", member.getType());

        LocalDateTime accessExpiredAt = LocalDateTime.now().plusMinutes(ACCESS_TOKEN_PLUS_MINUTE);
        String accessToken = Jwts.builder()
                .signWith(key)
                .setClaims(claim)
                .setExpiration(generateTime(accessExpiredAt))
                .compact();

        return TokenDto.builder()
                .token(accessToken)
                .expiredAt(accessExpiredAt)
                .build();
    }

    public TokenDto regenerateAccessToken(Map<String, Object> userData) {
        String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());

        LocalDateTime accessExpiredAt = LocalDateTime.now().plusMinutes(ACCESS_TOKEN_PLUS_MINUTE);
        String accessToken = Jwts.builder()
                .signWith(key)
                .setClaims(userData)
                .setExpiration(generateTime(accessExpiredAt))
                .compact();

        return TokenDto.builder()
                .token(accessToken)
                .expiredAt(accessExpiredAt)
                .build();
    }

    // 만료시간(토큰 발급 시간(현재) + n시간) 생성하는 메서드
    public Date generateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(
                ZoneId.systemDefault()
        ).toInstant());
    }

    // Refresh Token 발행 메서드
    // 민감한 정보 포함 x
    public TokenDto generateRefreshToken() {
        String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());

        LocalDateTime refreshExpiredAt = LocalDateTime.now().plusHours(REFRESH_TOKEN_PLUS_HOUR);
        String refreshToken = Jwts.builder()
                .signWith(key)
                .setExpiration(generateTime(refreshExpiredAt))
                .compact();
        return TokenDto.builder()
                .token(refreshToken)
                .expiredAt(refreshExpiredAt)
                .build();
    }

    public BothTokensDto getBothTokens(TokenDto accessToken, TokenDto refreshToken){
        return BothTokensDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 검증 메서드
    // 문자열 토큰을 집어넣으면 decode 하여 map 형태로 반환
    public Map<String, Object> validateTokenAndThrow(String token) {
        String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(base64EncodedKey.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            Jws<Claims> result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getBody());
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                throw new SignatureException("서명 오류");
            } else if (e instanceof ExpiredJwtException) {
                throw new ExpiredJwtException(null,null,"토큰 만료");
            } else {
                throw new RuntimeException("예외발생");
            }
        }
    }

    // JWT 해석(만료/유효성 체크 x)
    public Map decodeTokenAndThrow(String token) {
        String[] tokenParts = token.split("\\.");
        String decodedClaim = new String(
                Base64.getDecoder().decode(tokenParts[1]),
                StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(decodedClaim, Map.class);
        } catch (JsonProcessingException e) {
            throw new ResultException(TokenErrorCode.TOKEN_EXCEPTION);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        // 전달받은 refresh token으로 DB에 저장된 refresh token과 일치하는 entity 가져옴
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository
                .findFirstByToken(refreshToken)
                .orElseThrow(() -> new ResultException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND));

        // refresh token이 만료되었는지, 유효한지 체크
        validateTokenAndThrow(refreshToken);
    }

    public TokenDto reissueAccessToken(String expiredAccessToken) {
        Map<String, Object> userData = decodeTokenAndThrow(expiredAccessToken);

        return regenerateAccessToken(userData);
    }
}
