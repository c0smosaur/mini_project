package api.service;

import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.converter.ReservationConverter;
import api.model.request.ReservationRequest;
import api.model.response.ReservationResponse;
import db.entity.CartEntity;
import db.entity.MemberEntity;
import db.entity.ReservationEntity;
import db.enums.CartStatus;
import db.repository.CartRepository;
import db.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final MemberUtil memberUtil;

    // CartResponse로 돌려줄 필요 x
    public void getCartAndChangeStatusToN(Long cartId){
        Optional<CartEntity> cartEntity = cartRepository.findFirstById(cartId);
        if (cartEntity.isPresent()){
            cartEntity.get().setStatus(CartStatus.N);
            cartRepository.save(cartEntity.get());
        }
    }

    // 날짜 유효성 검증
    public boolean validateStartAndEndDate(ReservationRequest request){
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        // startDate가 endDate보다 전 (O)
        if (startDate.isBefore(endDate)){
            LocalDate currentDate = LocalDate.now();
            // 오늘 날짜가 startDate보다 전이거나 같음
            return currentDate.isBefore(startDate) || currentDate.isEqual(startDate);
        }
        // startDate가 endDate보다 나중 (X)
        return false;
    }

    // 예약 추가
    public ReservationResponse addReservation(ReservationRequest request) {
        // 현재 유저 식별번호 받아옴
        MemberEntity memberEntity = memberUtil.getCurrentMember();

        // 예약 날짜 유효성 확인
        if (!validateStartAndEndDate(request)){
            throw new ResultException(ReservationErrorCode.WRONG_DATE);
        }

        // member id 넣어주기
        request.setMemberId(memberEntity.getId());

        ReservationEntity entity = reservationConverter.toEntity(request);
        ReservationEntity newEntity = reservationRepository.save(entity);
        ReservationResponse response = reservationConverter.toResponse(newEntity);
        return response;
    }
}
