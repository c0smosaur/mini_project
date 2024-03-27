package api.service;

import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;
import db.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartConverter cartConverter;

    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<CartResponse> getAllCarts(Pageable pageable) {
        Page<CartResponse> page = cartRepository.findAll(pageable)
                .map(cartConverter::toResponse);
        return page.getContent();
    }

    // 장바구니 담기
    // TODO : memberID 할당
    public void addCart(CartRequest request) {
        CartEntity entity = cartConverter.toEntity(request);
        cartRepository.save(entity);
    }
}
