package api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationErrorCode implements ErrorCodeMold{

    NULL_RESERVATION(400, 2201,"빈 예약"),
    BAD_REQUEST(400, 2202, "잘못된 요청"),
    WRONG_DATE(400,2203,"날짜 오류"),
    NONEXISTENT_DATA(400, 2204, "존재하지 않는 데이터"),
    ROOM_UNAVAILABLE(400, 2205, "방 재고 없음"),
    CAPACITY_REACHED(400, 2206, "인원 수 초과"),
    UNACCEPTABLE_INPUT(400, 2207, "잘못된 입력"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
