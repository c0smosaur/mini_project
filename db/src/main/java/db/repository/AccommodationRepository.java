package db.repository;

import db.entity.AccommodationEntity;
import db.enums.AccommodationCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    @Query("select a from accommodation a join fetch a.rooms")
    Slice<AccommodationEntity> findAllFetchJoin(Pageable pageable);

    @Query("select a from accommodation a join fetch a.rooms where a.category = :category")
    Slice<AccommodationEntity> findAllByCategory(AccommodationCategory category, Pageable pageable);

    Optional<AccommodationEntity> findFirstById(Long id);
}