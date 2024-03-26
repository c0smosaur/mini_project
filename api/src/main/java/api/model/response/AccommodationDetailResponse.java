package api.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class AccommodationDetailResponse {
    private AccommodationResponse accommodation;
    private List<RoomResponse> rooms;
}
