package api.service;

import api.common.error.GeneralErrorCode;
import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.common.util.ValidationUtil;
import api.converter.ReservationConverter;
import api.model.request.ReservationRequest;
import api.model.response.ReservationResponse;
import db.entity.CartEntity;
import db.entity.MemberEntity;
import db.entity.ReservationEntity;
import db.entity.RoomEntity;
import db.repository.CartRepository;
import db.repository.ReservationRepository;
import db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final RoomRepository roomRepository;
    private final MemberUtil memberUtil;
    private final ValidationUtil validationUtil;

    // 예약 추가
    @Transactional
    public ReservationResponse addReservation(ReservationRequest request) {
        // 1. 현재 유저 식별번호 받아옴
        // member id 입력
        Long memberId = memberUtil.getCurrentMember();
        request.setMemberId(memberId);

        // 2. 예약 날짜 유효성 확인
        // 3. room 재고 수 차감
        validationUtil.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        validateAndModifyRoomEntity(request);

        // reservation 저장
        ReservationEntity entity = reservationConverter.toEntity(request);
        ReservationEntity newEntity = reservationRepository.save(entity);
        return reservationConverter.toResponse(newEntity);
    }

    @Transactional
    public void modifyCartStatus(Long cartId){
        Optional<CartEntity> cartEntity = cartRepository.findFirstByIdAndStatus(cartId, true);
        cartEntity.ifPresent(entity -> entity.setStatus(false));
    }

    // 방 있는지 / stock이 1인지 확인 후 stock 차감
    public void validateAndModifyRoomEntity(ReservationRequest request) {
        Optional<RoomEntity> roomEntity = roomRepository.findFirstById(request.getRoomId());
        if (roomEntity.isPresent()) {
            RoomEntity entity = roomEntity.get();

            if (!entity.validateRoomCapacity(request.getCapacity(), entity)){
                throw new ResultException(GeneralErrorCode.BAD_REQUEST);
            }
//            modifyRoomStock(entity);
        } else throw new ResultException(GeneralErrorCode.NOT_FOUND);
    }

    // 방 재고 차감
    // 재고가 1이어서 사용x
    public void modifyRoomStock(RoomEntity roomEntity){
        Integer defaultRoomStock = 1;
        if (Objects.equals(roomEntity.getStock(), defaultRoomStock)){
            roomEntity.setStock(roomEntity.getStock()-1);
            roomRepository.save(roomEntity);
        }
        else throw new ResultException(ReservationErrorCode.ROOM_UNAVAILABLE);
    }
}
