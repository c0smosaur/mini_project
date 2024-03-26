package api.controller;

import api.common.result.ResultWrapper;
import api.model.response.CartResponse;
import api.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ResultWrapper<List<CartResponse>>> getAllCarts(final Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(cartService.getAllCarts(pageable)));
    }
}
