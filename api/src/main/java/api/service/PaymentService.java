package api.service;

import api.common.error.GeneralErrorCode;
import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
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

    @Transactional
    public void getCartAndChangeStatusToN(Long cartId){
        Optional<CartEntity> cartEntity = cartRepository.findFirstByIdAndStatus(cartId, true);
        if (cartEntity.isPresent()){
            cartEntity.get().setStatus(false);
            cartRepository.save(cartEntity.get());
        }
    }

    // 날짜 유효성 검증
    public void validateStartAndEndDate(LocalDate startDate,
                                           LocalDate endDate){
        // startDate가 endDate보다 전 (O)
        if (startDate.isBefore(endDate)){
            LocalDate currentDate = LocalDate.now();
            // 오늘 날짜가 startDate보다 뒤 (X)
            if(currentDate.isAfter(startDate)){
                throw new ResultException(ReservationErrorCode.WRONG_DATE);
            }
        }
        // startDate가 endDate보다 나중 (X)
        else throw new ResultException(ReservationErrorCode.WRONG_DATE);
    }

    // 방 있는지 / stock이 1인지 확인 후 stock 차감
    public void validateAndModifyRoomEntity(ReservationRequest request) {
        Optional<RoomEntity> roomEntity = roomRepository.findFirstById(request.getRoomId());
        if (roomEntity.isPresent()) {
            RoomEntity entity = roomEntity.get();

            validateRoomCapacity(request.getCapacity(), entity);
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

    // 입력된 인원 수 검증
    public void validateRoomCapacity(Integer capacity, RoomEntity roomEntity){
        if (capacity > 0) {
            if(capacity > roomEntity.getMaxCapacity()){
                throw new ResultException(ReservationErrorCode.CAPACITY_REACHED);
            }
        } else throw new ResultException(ReservationErrorCode.UNACCEPTABLE_INPUT);
    }

    // 예약 추가
    @Transactional
    public ReservationResponse addReservation(ReservationRequest request) {
        // 1. 현재 유저 식별번호 받아옴
        // member id 입력
        MemberEntity memberEntity = memberUtil.getCurrentMember();
        request.setMemberId(memberEntity.getId());

        // 2. 예약 날짜 유효성 확인
        // 3. room 재고 수 차감
        validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        validateAndModifyRoomEntity(request);

        // reservation 저장
        ReservationEntity entity = reservationConverter.toEntity(request);
        ReservationEntity newEntity = reservationRepository.save(entity);
        return reservationConverter.toResponse(newEntity);
    }
}
