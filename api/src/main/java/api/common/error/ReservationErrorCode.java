package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationErrorCode implements ErrorCodeMold{

    NULL_RESERVATION(400, 2201,"빈 예약"),
    BAD_REQUEST(400, 2202, "잘못된 요청"),
    WRONG_DATE(400,2203,"날짜 오류"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
