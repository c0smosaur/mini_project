package api.config.oauth2;

import api.common.util.CookieUtil;
import api.config.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static api.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository
            httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${oauth.}")

    private Boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);
    }

    public String getUrl(HttpServletRequest request,
                         HttpServletResponse response,
                         Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie
                        (request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if (redirectUri.isPresent() && isAuthorized)

    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
    throws IOException {

    }

}
