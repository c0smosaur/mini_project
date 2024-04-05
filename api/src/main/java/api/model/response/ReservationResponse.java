package api.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationResponse {

    private Long id;
    private Long roomId;
    private Integer capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalPrice;

}
