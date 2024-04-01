package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.request.CartRequest;
import api.model.response.*;
import db.entity.CartEntity;
import db.entity.RoomEntity;


import java.util.Optional;

@Converter
public class CartConverter {

    // 장바구니 보기 시 응답 객체로 변환
    public CartResponse toResponse(CartEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> CartResponse.builder()
                        .id(it.getMemberId())
                        .memberId(it.getMemberId())
                        .roomId(it.getRoomId())
                        .capacity(it.getCapacity())
                        .startDate(it.getStartDate())
                        .endDate(it.getEndDate())
                        .totalPrice(it.getTotalPrice())
                        .build())
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));
    }

    // 장바구니 객체로 변환
    public CartEntity toEntity(CartRequest request, Long memberId) {
        return Optional.ofNullable(request)
                .map(it -> CartEntity.builder()
                        .memberId(memberId)
                        .roomId(it.getRoomId())
                        .capacity(it.getCapacity())
                        .startDate(it.getStartDate())
                        .endDate(it.getEndDate())
                        .status(true)
                        .totalPrice(it.getTotalPrice())
                        .build())
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));
    }

    // 장바구니 보기 시 응답 객체로 변환
    public AccommodationCartResponse toResponse(AccommodationResponse accommodation,
                                                CartEntity cartEntity,
                                                RoomEntity roomEntity){
        CartResponseWithPrice cart = CartResponseWithPrice.builder()
                .id(cartEntity.getId())
                .roomId(cartEntity.getRoomId())
                .maxCapacity(roomEntity.getMaxCapacity())
                .capacity(cartEntity.getCapacity())
                .startDate(cartEntity.getStartDate())
                .endDate(cartEntity.getEndDate())
                .roomPrice(roomEntity.getPrice())
                .totalPrice(cartEntity.getTotalPrice())
                .build();

        return AccommodationCartResponse.builder()
                .accommodation(accommodation)
                .cart(cart)
                .build();

    }
}
