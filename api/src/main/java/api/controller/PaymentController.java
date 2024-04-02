package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.CartReservationRequest;
import api.model.request.ReservationRequest;
import api.model.response.ReservationResponse;
import api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/reservation")
    // 결제 후 reservation 저장
    public ResultWrapper<ReservationResponse> addReservation(
            @RequestBody ReservationRequest request) {
        ReservationResponse response = paymentService.addReservation(request);
        return ResultWrapper.OK(response);
    }

    @PostMapping("/cart-reservation")
    public ResponseEntity<ResultWrapper<ReservationResponse>> modifyCartAndAddReservation(
            @RequestBody CartReservationRequest request) {
        paymentService.getCartAndChangeStatusToN(request.getCartId());
        ReservationResponse response = paymentService.addReservation(request.getReservation());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(response));
    }
}
