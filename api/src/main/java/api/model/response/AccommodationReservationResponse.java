package api.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationReservationResponse {
    private AccommodationResponse accommodation;
    private ReservationResponseWithPrice reservation;
}
