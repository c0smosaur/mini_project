package api.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartRequest {
    private Long roomId;
    private Integer capacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalPrice;
}