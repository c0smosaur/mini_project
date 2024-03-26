package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.response.CartResponse;
import db.entity.CartEntity;


import java.util.Optional;

@Converter
public class CartConverter {

    public CartResponse toResponse(CartEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    return CartResponse.builder()
                            .id(it.getMemberId())
                            .roomId(it.getRoomId())
                            .capacity(it.getCapacity())
                            .startDate(it.getStartDate())
                            .endDate(it.getEndDate())
                            .totalPrice(it.getTotalPrice())
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));
    }
}
