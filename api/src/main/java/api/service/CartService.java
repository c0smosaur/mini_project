package api.service;

import api.converter.CartConverter;
import api.model.response.CartResponse;
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

    @Transactional(readOnly = true)
    public List<CartResponse> getAllCarts(Pageable pageable) {
        Page<CartResponse> page = cartRepository.findAll(pageable)
                .map(cartConverter::toResponse);
        return page.getContent();
    }
}
