package api.converter;

import api.common.annotation.Converter;
import api.common.error.ReservationErrorCode;
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
        return Optional.ofNullable(entity)
                .map(it -> ReservationResponse.builder()
                        .id(it.getId())
                        .roomId(it.getRoomId())
                        .capacity(it.getCapacity())
                        .startDate(it.getStartDate())
                        .endDate(it.getEndDate())
                        .totalPrice(it.getTotalPrice())
                        .build())
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));
    }

    public ReservationEntity toEntity(ReservationRequest request) {
        return Optional.ofNullable(request)
                .map(it -> ReservationEntity.builder()
                        .roomId(it.getRoomId())
                        .memberId(it.getMemberId())
                        .capacity(it.getCapacity())
                        .startDate(it.getStartDate())
                        .endDate(it.getEndDate())
                        .totalPrice(it.getTotalPrice())
                        .build()).orElseThrow(() -> new ResultException(ReservationErrorCode.BAD_REQUEST));
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
