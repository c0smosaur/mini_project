package api.service;

import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.converter.ReservationConverter;
import api.model.response.ReservationResponse;
import db.entity.MemberEntity;
import db.entity.ReservationEntity;
import db.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;
    private final MemberUtil memberUtil;


    // 숙소 객실별 예약 조회
    public List<ReservationResponse> getAllReservationForRoom(Long roomId){
        List<ReservationEntity> list = reservationRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return list.stream().map(reservationConverter::toResponse).toList();
    }

    // 단일 예약 조회
    public ReservationResponse getReservation(Long reservationId){
        ReservationEntity entity = reservationRepository.findFirstById(reservationId)
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));

        return reservationConverter.toResponse(entity);
    }

    // 사용자의 예약 전체 조회
    public List<ReservationResponse> getUserReservationAll(){
        MemberEntity memberEntity = memberUtil.getCurrentMember();

        List<ReservationEntity> list = reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberEntity.getId());

        return list.stream()
                .map(reservationConverter::toResponse).toList();
    }
}
