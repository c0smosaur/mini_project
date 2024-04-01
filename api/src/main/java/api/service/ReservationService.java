package api.service;

import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import api.common.util.MemberUtil;
import api.converter.AccommodationConverter;
import api.converter.ReservationConverter;
import api.model.response.AccommodationReservationResponse;
import api.model.response.AccommodationResponse;
import api.model.response.ReservationResponse;
import db.entity.AccommodationEntity;
import db.entity.MemberEntity;
import db.entity.ReservationEntity;
import db.entity.RoomEntity;
import db.repository.AccommodationRepository;
import db.repository.ReservationRepository;
import db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final ReservationConverter reservationConverter;
    private final AccommodationConverter accommodationConverter;
    private final MemberUtil memberUtil;


    // 숙소 객실별 예약 조회
    public List<ReservationResponse> getAllReservationForRoom(Long roomId){
        List<ReservationEntity> reservations = reservationRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return reservations.stream().map(reservationConverter::toResponse).toList();
    }

    // 단일 예약 조회
    public AccommodationReservationResponse getReservation(Long reservationId){
        ReservationEntity reservationEntity = reservationRepository.findFirstById(reservationId)
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));
        RoomEntity roomEntity = roomRepository.findFirstById(reservationEntity.getRoomId())
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));
        AccommodationEntity accommodationEntity = accommodationRepository.findFirstById(roomEntity.getAccommodationId())
                .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));

        return reservationConverter.toResponse(entity);
        AccommodationResponse accommodation = accommodationConverter.toResponse(accommodationEntity);

        return reservationConverter.toResponse(
                accommodation, reservationEntity, roomEntity);
    }

    // 사용자의 예약 전체 조회
    public List<AccommodationReservationResponse> getUserReservationAll(){
        MemberEntity memberEntity = memberUtil.getCurrentMember();

        List<ReservationEntity> list = reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberEntity.getId());

        return list.stream()
                .map(reservationEntity -> {
                    RoomEntity roomEntity = roomRepository.findFirstById(reservationEntity.getRoomId())
                            .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));
                    AccommodationEntity accommodationEntity = accommodationRepository.findFirstById(roomEntity.getAccommodationId())
                            .orElseThrow(() -> new ResultException(ReservationErrorCode.NULL_RESERVATION));

                    AccommodationResponse accommodation = accommodationConverter.toResponse(accommodationEntity);
                    return reservationConverter.toResponse(
                            accommodation, reservationEntity, roomEntity);
                })
                .collect(Collectors.toList());
    }
}
