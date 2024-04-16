package api.config.jwt;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TokenDto {
    private String token;
    private LocalDateTime expiredAt;

}
