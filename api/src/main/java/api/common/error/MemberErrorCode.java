package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeMold{

    DUPLICATE_USERNAME(400,2100, "중복 이메일 주소"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
