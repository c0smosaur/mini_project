package api.converter;

import api.common.annotation.Converter;
import api.model.response.AccommodationResponse;
import db.entity.AccommodationEntity;

@Converter
public class AccommodationConverter {
    public AccommodationResponse toResponse(AccommodationEntity entity) {
        return AccommodationResponse.builder()
                .title(entity.getTitle())
                .address(entity.getAddress())
                .image1(entity.getImage1())
                .image2(entity.getImage2())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .tel(entity.getTel())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
