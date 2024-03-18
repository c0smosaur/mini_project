package db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {

    CART("장바구니"),
    HISTORY("결제완료"),
    ;

    private String description;
}
