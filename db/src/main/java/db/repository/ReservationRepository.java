package db.repository;

import db.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findAllByRoomIdOrderByCreatedAtDesc(Long roomId);
    Optional<ReservationEntity> findFirstById(Long id);
    List<ReservationEntity> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}