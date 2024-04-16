package api.config.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BothTokensDto {
    private TokenDto accessToken;
    private TokenDto refreshToken;
}
