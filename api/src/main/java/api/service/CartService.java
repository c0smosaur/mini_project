package api.service;

import api.common.util.MemberUtil;
import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;

import db.repository.CartRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
