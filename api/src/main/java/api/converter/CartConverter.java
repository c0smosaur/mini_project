package api.converter;

import api.common.annotation.Converter;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.request.CartRequest;
import api.model.response.AccommodationCartResponse;
import api.model.response.AccommodationResponse;
import api.model.response.CartResponse;
import api.model.response.CartResponseWithPrice;
import db.entity.CartEntity;
import db.entity.RoomEntity;

import java.util.Optional;

@Converter
public class CartConverter {

    // 장바구니 보기 시 응답 객체로 변환
    public CartResponse toResponse(CartEntity entity) {
        return CartResponse.builder()
                .id(entity.getMemberId())
                .memberId(entity.getMemberId())
                .roomId(entity.getRoomId())
                .capacity(entity.getCapacity())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    // 장바구니 객체로 변환
    public CartEntity toEntity(CartRequest request, Long memberId) {
        return CartEntity.builder()
                .memberId(memberId)
                .roomId(request.getRoomId())
                .capacity(request.getCapacity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(true)
                .totalPrice(request.getTotalPrice())
                .build();
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
