package db.repository;

import db.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findFirstByToken(String token);

    Optional<RefreshTokenEntity> findFirstByMemberId(Long memberId);
}
