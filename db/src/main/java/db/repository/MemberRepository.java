package db.repository;

import db.entity.MemberEntity;
import db.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findFirstByUsernameAndStatus(String username, MemberStatus status);

    Optional<MemberEntity> findFirstByIdAndStatus(Long id, MemberStatus status);
}
