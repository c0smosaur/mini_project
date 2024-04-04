package api.config.auth;

import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.JwtProvider;
import api.config.jwt.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Order(0)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    /** 헤더에서 토큰 받아오기 **/
    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    /** 토큰에서 user 정보 받아오기 **/
    private User parseUserFromToken(String token){
        Map<String, Object> parsedToken = Optional.ofNullable(token)
                .map(jwtProvider::validateTokenAndThrow)
                .orElse(Map.of("username",
                        "anonymous",
                        "type",
                        "anonymous"));

        return createUser(parsedToken);
    }

    private User createUser(Map<String, Object> map){
        String username = (String) map.get("username");
        String type = (String) map.get("type");

        return new User(username, "", List.of(new SimpleGrantedAuthority(type)));
    }

    /** access token 재발행
     * 1) refresh token 받아오기
     * 2) refresh token 검증
     * 3) refresh token 으로 access token 재발행
     * */
    private String issueAccessTokenByRefreshToken(HttpServletRequest request,
                                           HttpServletResponse response){
        try{
            String refreshToken = parseBearerToken(request, "RefreshToken");
            if (refreshToken==null){
                throw new ResultException(TokenErrorCode.EXPIRED_TOKEN, "refresh token 필요");
            }

            // refresh token 검증
            jwtProvider.validateRefreshToken(refreshToken);

            // 만료된 access token으로 새 token 발행
            String expiredAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            TokenDto newAccessToken = jwtProvider.reissueAccessToken(expiredAccessToken);

            return newAccessToken.getToken();
        } catch (Exception e) {
            throw new ResultException(TokenErrorCode.TOKEN_EXCEPTION);
        }
    }

    private void storeAuthenticationInContext(HttpServletRequest request, String token){
        User user = parseUserFromToken(token);
        AbstractAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.
                        authenticated(
                                user,
                                token,
                                user.getAuthorities());
        authenticated.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        try{
            String token = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            storeAuthenticationInContext(request, token);

            // 토큰 만료 시 Refresh Token 확인하여 Access Token 재발급
        } catch (ExpiredJwtException e){

            String newAccessToken = issueAccessTokenByRefreshToken(request, response);
            storeAuthenticationInContext(request, newAccessToken);

            // 헤더에 New-Access-Token으로 새 access token 전달
            response.setHeader("NewAccessToken",newAccessToken);

            // 유효하지 않은 토큰 혹은 그 외 오류 발생 시 로그아웃처리, 다시 로그인하도록 유도
        } catch (Exception e){
            // printStackTrace는 개발 단계에서만 사용
//            e.printStackTrace();

            throw new ResultException(TokenErrorCode.TOKEN_EXCEPTION);
        }

        filterChain.doFilter(request, response);
    }
}
