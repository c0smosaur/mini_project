package api.service;

import api.common.util.MemberUtil;
import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;
import db.enums.CartStatus;
import db.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartConverter cartConverter;
    private final MemberUtil memberUtil;

    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<CartResponse> getAllCartsByMemberId() {
        return cartRepository.findAllByMemberId(memberUtil.getCurrentMember().getId())
                .stream()
                .map(cartConverter::toResponse)
                .toList();
    }

    // 장바구니 담기
    public void addCart(CartRequest request) {
        CartEntity entity = cartConverter.toEntity(request, memberUtil.getCurrentMember().getId());

        cartRepository.save(entity);
    }

    public void getCartAndChangeStatus(Long cartId, CartStatus cartStatus) {
        Optional<CartEntity> cartEntity = cartRepository.findFirstById(cartId);
        if (cartEntity.isPresent()) {
            cartEntity.get().setStatus(cartStatus);
            cartRepository.save(cartEntity.get());
        }
    }
}
