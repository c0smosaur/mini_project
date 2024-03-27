package db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CartStatus {

    Y("장바구니 O"),
    N("장바구니 X"),
    ;
    private String status;
}