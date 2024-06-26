package api.service;

import api.common.error.CartErrorCode;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.common.util.ValidationUtil;
import api.converter.AccommodationConverter;
import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.response.AccommodationCartResponse;
import api.model.response.AccommodationResponse;
import db.entity.AccommodationEntity;
import db.entity.CartEntity;
import db.entity.RoomEntity;
import db.repository.AccommodationRepository;
import db.repository.CartRepository;
import db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final CartConverter cartConverter;
    private final AccommodationConverter accommodationConverter;
    private final MemberUtil memberUtil;
    private final ValidationUtil validationUtil;

    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<AccommodationCartResponse> getMemberCarts() {

        List<CartEntity> list = cartRepository.findAllByMemberIdAndStatusOrderByCreatedAtDesc(
                memberUtil.getCurrentMember(),
                true);

        return list.stream()
                .map(cartEntity -> {
                    RoomEntity roomEntity = roomRepository.findFirstById(cartEntity.getRoomId())
                            .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));
                    AccommodationEntity accommodationEntity = accommodationRepository.findFirstById(roomEntity.getAccommodation().getId())
                            .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));

                    AccommodationResponse accommodation = accommodationConverter.toResponse(accommodationEntity);
                    return cartConverter.toResponse(
                            accommodation, cartEntity, roomEntity);
                })
                .collect(Collectors.toList());
    }

    // 장바구니 담기
    @Transactional
    public void addCart(CartRequest request) {
        validationUtil.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        validateCartByRoomEntity(request);
        CartEntity entity = cartConverter.toEntity(request, memberUtil.getCurrentMember());

        cartRepository.save(entity);
    }

    public void validateCartByRoomEntity(CartRequest request) {
        Optional<RoomEntity> roomEntity = roomRepository.findFirstById(request.getRoomId());
        if (roomEntity.isPresent()) {
            RoomEntity entity = roomEntity.get();
            if (!entity.validateRoomCapacity(request.getCapacity(), entity)) {
                throw new ResultException(GeneralErrorCode.BAD_REQUEST);
            }
        } else throw new ResultException(CartErrorCode.NONEXISTENT_DATA);
    }

    // 카트 status 수정
    @Transactional
    public Optional<CartEntity> modifyCart(Long cartId, Boolean cartStatus) {
        Optional<CartEntity> cartEntity = cartRepository.findFirstByIdAndStatus(cartId, cartStatus);
        cartEntity.ifPresent(entity -> entity.setStatus(false));
        return cartEntity;
    }
}
