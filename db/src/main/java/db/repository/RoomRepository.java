package db.repository;

import db.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findAllByAccommodationId(Long accommodationId);

    Optional<RoomEntity> findFirstById(Long id);
}
