package db.repository;

import db.entity.MemberEntity;
import db.enums.MemberStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findFirstByUsernameAndStatus(String username, MemberStatus status);

    Optional<MemberEntity> findFirstByIdAndStatus(Long id, MemberStatus status);

//    @Override
//    @Lock(LockModeType.PESSIMISTIC_READ)

}
