package api.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationCartResponse {
    private AccommodationResponse accommodation;
    private CartResponseWithPrice cart;
}
