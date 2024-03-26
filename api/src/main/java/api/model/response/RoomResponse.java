package api.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class RoomResponse {
    private Long id;
    private Integer maxCapacity;
    private BigDecimal price;
    private Integer stock;
}
