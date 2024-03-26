package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeMold{

    DUPLICATE_USERNAME(400,2100, "중복 이메일 주소"),
    IMAGE_ERROR(400,2101,"프로필 이미지 오류"),
    USER_DOES_NOT_EXIST(400, 2102,"존재하지 않는 유저"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
