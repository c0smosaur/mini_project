package db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberStatus {

    REGISTERED("등록"),
    UNREGISTERED("해지"),
    ;
    private String description;
}
