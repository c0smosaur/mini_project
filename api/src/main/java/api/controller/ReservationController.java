package api.controller;

import api.common.result.ResultWrapper;
import api.model.response.AccommodationReservationResponse;
import api.model.response.ReservationResponse;
import api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 특정 방의 예약 조회
    @GetMapping()
    public ResultWrapper<List<ReservationResponse>> viewReservationForRoom(
            @RequestParam(name = "room") Long roomId){
        List<ReservationResponse> responseList = reservationService.getAllReservationForRoom(roomId);
        return ResultWrapper.OK(responseList);
    }

    // 예약 내역 확인
    @GetMapping("/{reservationId}")
    public ResultWrapper<AccommodationReservationResponse> confirmReservation(
            @PathVariable Long reservationId){
        AccommodationReservationResponse response = reservationService.getReservation(reservationId);
        return ResultWrapper.OK(response);
    }

    // 사용자의 과거 예약 내역 전체 조회
    @GetMapping("/history")
    public ResultWrapper<List<AccommodationReservationResponse>> viewUserReservationsAll(){
        List<AccommodationReservationResponse> responseList = reservationService.getUserReservationAll();
        return ResultWrapper.OK(responseList);
    }
}
