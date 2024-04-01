package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements ErrorCodeMold{
    OK(200, 200, "성공"),
    BAD_REQUEST(400, 400, "잘못된 요청"),
    NOT_FOUND(404, 404, "찾을 수 없음"),
    SERVER_ERROR(500, 500, "서버 오류"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point 에러"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
