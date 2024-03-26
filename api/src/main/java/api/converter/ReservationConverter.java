package api.converter;

import api.common.annotation.Converter;
import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.model.request.ReservationRequest;
import api.model.response.ReservationResponse;
import db.entity.ReservationEntity;

import java.util.Optional;

@Converter
public class ReservationConverter {

    public ReservationResponse toResponse(ReservationEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    return ReservationResponse.builder()
                            .id(it.getId())
                            .roomId(it.getRoomId())
                            .capacity(it.getCapacity())
                            .startDate(it.getStartDate())
                            .endDate(it.getEndDate())
                            .totalPrice(it.getTotalPrice())
                            .build();
                })
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));
    }

    public ReservationEntity toEntity(ReservationRequest request) {
        return Optional.ofNullable(request)
                .map(it -> {
                    return ReservationEntity.builder()
                            .roomId(it.getRoomId())
                            .memberId(it.getMemberId())
                            .capacity(it.getCapacity())
                            .startDate(it.getStartDate())
                            .endDate(it.getEndDate())
                            .totalPrice(it.getTotalPrice())
                            .build();
                }).orElseThrow(() -> new ResultException(ReservationErrorCode.BAD_REQUEST));
    }

}
