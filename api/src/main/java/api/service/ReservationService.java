package api.service;

import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.converter.ReservationConverter;
import api.model.request.ReservationRequest;
import api.model.response.ReservationResponse;
import db.entity.MemberEntity;
import db.entity.ReservationEntity;
import db.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final MemberUtil memberUtil;

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

    // 숙소 객실별 예약 조회
    public List<ReservationResponse> getAllReservationForRoom(Long roomId){
        List<ReservationEntity> list = reservationRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        List<ReservationResponse> responseList = list.stream().map(reservationConverter::toResponse).toList();
        return responseList;
    }

    // 단일 예약 조회
    public ReservationResponse getReservation(Long reservationId){
        ReservationEntity entity = reservationRepository.findFirstById(reservationId)
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));

        ReservationResponse response = reservationConverter.toResponse(entity);
        return response;
    }

    // 사용자의 예약 전체 조회
    public List<ReservationResponse> getUserReservationAll(){
        MemberEntity memberEntity = memberUtil.getCurrentMember();

        List<ReservationEntity> list = reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberEntity.getId());
        List<ReservationResponse> responseList = list.stream()
                .map(reservationConverter::toResponse).toList();

        return responseList;
    }

    // cart로 전달받아 reservation에 추가

}
