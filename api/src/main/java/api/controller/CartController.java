package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.CartRequest;
import api.model.response.AccommodationCartResponse;
import api.service.CartService;
import db.entity.CartEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 보기
    @GetMapping
    public ResponseEntity<ResultWrapper<List<AccommodationCartResponse>>> getMemberCarts() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(cartService.getMemberCarts()));
    }

    // 장바구니 담기
    @PostMapping
    public ResponseEntity<ResultWrapper<Void>> addCart(@Valid @RequestBody CartRequest request) {
        cartService.addCart(request);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(null));
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ResultWrapper<Void>> deleteCart(
            @PathVariable Long cartId){
        Optional<CartEntity> cartEntity = cartService.getCartAndChangeStatus(cartId, true);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT.value())
                .body(ResultWrapper.OK(null));
    }
}
