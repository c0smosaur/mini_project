package db.repository;

import db.entity.AccommodationEntity;
import db.enums.AccommodationCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;
import java.util.Optional;

import static db.enums.AccommodationCategory.B02010100;
import static db.enums.AccommodationCategory.B02010200;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class AccommodationRepositoryTest {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @DisplayName("숙소 전체 조회")
    @Test
    void givenNothing_whenFindingAllAccommodations_thenReturnsAllAccommodations() {
        // Given

        // When
        Page<AccommodationEntity> accommodations = accommodationRepository.findAll(PageRequest.of(0, 20));

        // Then
        assertThat(accommodations).hasSize(20).map(AccommodationEntity::getAddress).allMatch(Objects::nonNull);
    }

    @DisplayName("숙소 카테고리별 조회")
    @Test
    void givenAccommodationCategory_whenFindingAccommodationsByCategory_thenReturnsAccommodations() {
        // Given
        AccommodationCategory category1 = B02010100;
        AccommodationCategory category2 = B02010200;

        // When
        Page<AccommodationEntity> accommodations1 = accommodationRepository.findByCategory(category1, PageRequest.of(0, 20));
        Page<AccommodationEntity> accommodations2 = accommodationRepository.findByCategory(category2, PageRequest.of(0, 20));

        // Then
        assertThat(accommodations1).hasSize(20);
        assertThat(accommodations2).hasSize(0);
    }

    @DisplayName("숙소 개별 조회 (상세 조회)")
    @Test
    void givenAccommodationId_whenFindingAccommodationById_thenReturnsAccommodation() {
        // Given
        Long accommodationId = 1L;

        // When
        Optional<AccommodationEntity> accommodation = accommodationRepository.findById(accommodationId);

        // Then
        assertThat(accommodation).isNotNull();
    }
}