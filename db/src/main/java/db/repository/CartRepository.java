package db.repository;

import db.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByMemberId(Long memberId);

    Optional<CartEntity> findFirstById(Long id);
}
