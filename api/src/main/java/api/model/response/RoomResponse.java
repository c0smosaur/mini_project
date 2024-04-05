package api.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RoomResponse {
    private Long id;
    private Integer maxCapacity;
    private Long price;
    private Integer stock;
}
