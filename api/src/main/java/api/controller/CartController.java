package api.controller;

import api.common.result.ResultWrapper;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import api.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 보기
    @GetMapping
    public ResponseEntity<ResultWrapper<List<CartResponse>>> getAllCartsByMemberId() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(cartService.getAllCartsByMemberIdAndStatus()));
    }

    // 장바구니 담기
    @PostMapping
    public ResponseEntity<ResultWrapper<Void>> addCart(@RequestBody CartRequest request) {
        cartService.addCart(request);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(null));
    }

    @DeleteMapping("/{cart-id}")
    public ResponseEntity<ResultWrapper<Void>> deleteCart(
            @PathVariable(name = "cart-id") Long cartId){
        cartService.getCartAndChangeStatus(cartId, false);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(null));
    }
}
