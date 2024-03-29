package db.repository;

import db.entity.AccommodationEntity;
import db.enums.AccommodationCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    Page<AccommodationEntity> findByCategory(AccommodationCategory category, Pageable pageable);

    Optional<AccommodationEntity> findFirstById(Long id);
}
