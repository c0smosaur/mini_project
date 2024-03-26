package db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationStatus {

    CART("장바구니"),
    HISTORY("결제완료"),
    ;

    private String description;
}
