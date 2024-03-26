package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.ReservationRequest;
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

    // 결제 후 reservation 저장
    @PostMapping("/payments")
    public ResultWrapper<ReservationResponse> addReservation(
            @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.addReservation(request);
        return ResultWrapper.OK(response);
    }

    // 특정 방의 예약 조회
    @GetMapping("/room/{room-id}")
    public ResultWrapper<List<ReservationResponse>> viewReservationForRoom(
            @PathVariable(name = "room-id") Long roomId){
        List<ReservationResponse> responseList = reservationService.getAllReservationForRoom(roomId);
        return ResultWrapper.OK(responseList);
    }

    // 예약 내역 확인
    @GetMapping("/{reservationId}")
    public ResultWrapper<ReservationResponse> confirmReservation(
            @PathVariable Long reservationId){
        ReservationResponse response = reservationService.getReservation(reservationId);
        return ResultWrapper.OK(response);
    }

    // 사용자의 과거 예약 내역 전체 조회
    @GetMapping("/history")
    public ResultWrapper<List<ReservationResponse>> viewUserReservationsAll(){
        List<ReservationResponse> responseList = reservationService.getUserReservationAll();
        return ResultWrapper.OK(responseList);
    }
}
