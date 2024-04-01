package api.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CartResponseWithPrice {
    private Long id;
    private Long roomId;
    private Integer maxCapacity;
    private Integer capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomPrice;
    private Long totalPrice;
}
