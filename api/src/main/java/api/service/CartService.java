package api.service;

import api.common.error.MemberErrorCode;
import api.common.exception.ResultException;
import api.converter.CartConverter;
import api.model.request.CartRequest;
import api.model.response.CartResponse;
import db.entity.CartEntity;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.repository.CartRepository;

import db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartConverter cartConverter;

    private final MemberRepository memberRepository;
    // 장바구니 보기
    @Transactional(readOnly = true)
    public List<CartResponse> getAllCartsByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(MemberErrorCode.USER_DOES_NOT_EXIST));

        return cartRepository.findAllByMemberId(memberEntity.getId())
                .stream()
                .map(cartConverter::toResponse)
                .toList();
    }

    // 장바구니 담기
    public void addCart(CartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(MemberErrorCode.USER_DOES_NOT_EXIST));

        CartEntity entity = cartConverter.toEntity(request, memberEntity.getId());

        cartRepository.save(entity);
    }
}
