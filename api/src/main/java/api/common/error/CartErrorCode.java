package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements ErrorCodeMold{

    WRONG_DATE(400,2303,"날짜 오류"),
    NONEXISTENT_DATA(400, 2304, "존재하지 않는 데이터"),
    CAPACITY_REACHED(400, 2306, "인원 수 초과"),
    UNACCEPTABLE_INPUT(400, 2307, "잘못된 입력"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
