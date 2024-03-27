package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;
import db.enums.CartStatus;


import java.util.Optional;

@Converter
public class CartConverter {

    // 장바구니 보기 시 응답 객체로 변환
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

    // 장바구니 객체로 변환
    public CartEntity toEntity(CartRequest request, Long memberId) {
        return Optional.ofNullable(request)
                .map(it -> {
                    return CartEntity.builder()
                            .memberId(memberId)
                            .roomId(it.getRoomId())
                            .capacity(it.getCapacity())
                            .startDate(it.getStartDate())
                            .endDate(it.getEndDate())
                            .status(CartStatus.Y)
                            .totalPrice(it.getTotalPrice())
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));
    }
}
