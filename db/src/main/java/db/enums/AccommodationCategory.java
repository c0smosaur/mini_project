package db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccommodationCategory {

    B02010100("관광호텔"),
    B02010200("수상관광호텔"),
    B02010300("전통호텔"),
    B02010400("가족호텔"),
    B02010500("콘도미니엄"),
    B02010600("유스호스텔"),
    B02010700("펜션"),
    B02010800("여관"),
    B02010900("모텔"),
    B02011000("민박"),
    B02011100("게스트하우스"),
    B02011200("홈스테이"),
    B02011300("서비스드레지던스"),
    B02011400("의료관광호텔"),
    B02011500("소형호텔"),
    B02011600("한옥"),
    ;

    private final String description;
}
