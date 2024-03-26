package api.model.request;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Long roomId;
    private Integer capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalPrice;
}