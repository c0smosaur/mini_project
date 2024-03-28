package api.service;

import api.common.error.CartErrorCode;
import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.request.ReservationRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;
import db.entity.RoomEntity;
import db.enums.CartStatus;
import db.repository.CartRepository;
import db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RoomRepository roomRepository;
    private final CartConverter cartConverter;
    private final MemberUtil memberUtil;

    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<CartResponse> getAllCartsByMemberId() {
        return cartRepository.findAllByMemberId(memberUtil.getCurrentMember().getId())
                .stream()
                .map(cartConverter::toResponse)
                .toList();
    }

    // 장바구니 담기
    public void addCart(CartRequest request) {
        validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        validateCartByRoomEntity(request);
        CartEntity entity = cartConverter.toEntity(request, memberUtil.getCurrentMember().getId());

        cartRepository.save(entity);
    }

    // 카트 status 수정
    public void getCartAndChangeStatus(Long cartId, CartStatus cartStatus) {
        Optional<CartEntity> cartEntity = cartRepository.findFirstById(cartId);
        if (cartEntity.isPresent()) {
            cartEntity.get().setStatus(cartStatus);
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
