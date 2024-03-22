package api.model.response;

import db.enums.AccommodationCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AccommodationResponse {
    private String title;
    private String address;
    private String image1;
    private String image2;
    private String description;
    private AccommodationCategory category;
}
