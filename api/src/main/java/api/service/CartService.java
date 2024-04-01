package api.service;

import api.common.error.CartErrorCode;
import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
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

import java.time.LocalDate;
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

    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<AccommodationCartResponse> getAllCartsByMemberIdAndStatus(){

        List<CartEntity> list = cartRepository.findAllByMemberIdAndStatusOrderByCreatedAtDesc(
                memberUtil.getCurrentMember().getId(),
                true);

        return list.stream()
                .map(cartEntity -> {
                    RoomEntity roomEntity = roomRepository.findFirstById(cartEntity.getRoomId())
                            .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));
                    AccommodationEntity accommodationEntity = accommodationRepository.findFirstById(roomEntity.getAccommodationId())
                            .orElseThrow(() -> new ResultException(GeneralErrorCode.NOT_FOUND));

                    AccommodationResponse accommodation = accommodationConverter.toResponse(accommodationEntity);
                    return cartConverter.toResponse(
                            accommodation, cartEntity, roomEntity);
                })
                .collect(Collectors.toList());
    }

    // 장바구니 담기
    public void addCart(CartRequest request) {
        validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        validateCartByRoomEntity(request);
        CartEntity entity = cartConverter.toEntity(request, memberUtil.getCurrentMember().getId());

        cartRepository.save(entity);
    }

    // 카트 status 수정
    public void getCartAndChangeStatus(Long cartId, Boolean cartStatus) {
        Optional<CartEntity> cartEntity = cartRepository.findFirstByIdAndStatus(cartId, cartStatus);
        if (cartEntity.isPresent()) {
            cartEntity.get().setStatus(false);
            cartRepository.save(cartEntity.get());
        }
    }

    // 날짜 유효성 검증
    public void validateStartAndEndDate(LocalDate startDate,
                                        LocalDate endDate) {
        // startDate가 endDate보다 전 (O)
        if (startDate.isBefore(endDate)) {
            LocalDate currentDate = LocalDate.now();
            // 오늘 날짜가 startDate보다 뒤 (X)
            if (currentDate.isAfter(startDate)) {
                throw new ResultException(CartErrorCode.WRONG_DATE);
            }
        }
        // startDate가 endDate보다 나중 (X)
        else throw new ResultException(CartErrorCode.WRONG_DATE);
    }

    // 입력된 인원 수 검증
    public void validateRoomCapacity(Integer capacity, RoomEntity roomEntity){
        if (capacity > 0) {
            if(capacity > roomEntity.getMaxCapacity()){
                throw new ResultException(CartErrorCode.CAPACITY_REACHED);
            }
        } else throw new ResultException(CartErrorCode.UNACCEPTABLE_INPUT);
    }

    public void validateCartByRoomEntity(CartRequest request) {
        Optional<RoomEntity> roomEntity = roomRepository.findFirstById(request.getRoomId());
        if (roomEntity.isPresent()) {
            RoomEntity entity = roomEntity.get();
            validateRoomCapacity(request.getCapacity(), entity);
        }
        else throw new ResultException(CartErrorCode.NONEXISTENT_DATA);
    }
}
