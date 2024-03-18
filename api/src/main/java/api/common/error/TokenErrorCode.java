package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCodeMold {

    INVALID_TOKEN(400, 2000, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(400, 2001, "만료된 토큰"),
    TOKEN_EXCEPTION(400, 2002, "알 수 없는 토큰 오류"),
    AUTHORIZATION_TOKEN_NOT_FOUND(400, 2003, "토큰이 존재하지 않음"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
