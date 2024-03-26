package api.converter;

import api.common.annotation.Converter;
import api.model.response.RoomResponse;
import db.entity.RoomEntity;

@Converter
public class RoomConverter {
    public RoomResponse toResponse(RoomEntity entity) {
        return RoomResponse.builder()
                .id(entity.getId())
                .maxCapacity(entity.getMaxCapacity())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }
}
