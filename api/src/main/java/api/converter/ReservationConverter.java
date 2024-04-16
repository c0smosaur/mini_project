package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.request.ReservationRequest;
import api.model.response.AccommodationReservationResponse;
import api.model.response.AccommodationResponse;
import api.model.response.ReservationResponse;
import api.model.response.ReservationResponseWithPrice;
import db.entity.ReservationEntity;
import db.entity.RoomEntity;

import java.util.Optional;

@Converter
public class ReservationConverter {

    public ReservationResponse toResponse(ReservationEntity entity) {
        return ReservationResponse.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .capacity(entity.getCapacity())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    public ReservationEntity toEntity(ReservationRequest request) {
        return ReservationEntity.builder()
                        .roomId(request.getRoomId())
                        .memberId(request.getMemberId())
                        .capacity(request.getCapacity())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .totalPrice(request.getTotalPrice())
                        .build();
    }
    
    public AccommodationReservationResponse toResponse(AccommodationResponse accommodation,
                                                       ReservationEntity reservationEntity,
                                                       RoomEntity roomEntity){
        ReservationResponseWithPrice reservation = ReservationResponseWithPrice.builder()
                .id(reservationEntity.getId())
                .roomId(reservationEntity.getRoomId())
                .maxCapacity(roomEntity.getMaxCapacity())
                .capacity(reservationEntity.getCapacity())
                .startDate(reservationEntity.getStartDate())
                .endDate(reservationEntity.getEndDate())
                .roomPrice(roomEntity.getPrice())
                .totalPrice(reservationEntity.getTotalPrice())
                .build();

        return AccommodationReservationResponse.builder()
                .accommodation(accommodation)
                .reservation(reservation)
                .build();

    }
}
