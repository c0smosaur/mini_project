package api.service;

import api.converter.AccommodationConverter;
import api.model.response.AccommodationResponse;
import db.entity.AccommodationEntity;
import db.entity.RoomEntity;
import db.enums.AccommodationCategory;
import db.repository.AccommodationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static db.enums.AccommodationCategory.B02010100;
import static db.enums.AccommodationCategory.B02011600;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {
    @InjectMocks
    private AccommodationService accommodationService;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationConverter accommodationConverter;

    @DisplayName("숙소 전체 조회")
    @Test
    void givenNothing_whenSearchingAllAccommodations_thenReturnsAllAccommodations() {
        // Given
        given(accommodationRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(
                        new AccommodationEntity(
                                "가경재 [한국관광 품질인증/Korea Quality]",
                                "경상북도 안동시 하회남촌길 69-5",
                                "http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg",
                                "http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg",
                                "경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]",
                                B02011600,
                                "054-855-8552",
                                36.537653745,
                                128.5175868107,
                                List.of(
                                        new RoomEntity(
                                                null, 2, 117500L, 1
                                        ),
                                        new RoomEntity(
                                                null, 4, 213300L, 1
                                        )
                                )
                        ),
                        new AccommodationEntity(
                                "가락관광호텔",
                                "서울특별시 송파구 송파대로28길 5",
                                "",
                                "",
                                "서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔",
                                B02010100,
                                "02-400-6641~3",
                                37.4966565128,
                                127.1166298703,
                                List.of(
                                        new RoomEntity(
                                                null, 2, 110000L, 1
                                        ),
                                        new RoomEntity(
                                                null, 4, 225000L, 1
                                        )
                                )
                        )
                )));

        // When
        List<AccommodationResponse> accommodations = accommodationService.getAllAccommodations(PageRequest.of(0, 20));

        // Then
        assertThat(accommodations).hasSize(2);
        then(accommodationRepository).should().findAll(any(Pageable.class));
    }

    @DisplayName("숙소 카테고리별 조회")
    @Test
    void givenAccommodationCategory_whenSearchingAccommodationsByCategory_thenReturnsAccommodations() {
        // Given
        given(accommodationRepository.findByCategory(any(AccommodationCategory.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(
                        new AccommodationEntity(
                                "가락관광호텔",
                                "서울특별시 송파구 송파대로28길 5",
                                "",
                                "",
                                "서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔",
                                B02010100,
                                "02-400-6641~3",
                                37.4966565128,
                                127.1166298703,
                                List.of(
                                        new RoomEntity(
                                                null, 2, 110000L, 1
                                        ),
                                        new RoomEntity(
                                                null, 4, 225000L, 1
                                        )
                                )
                        ),
                        new AccommodationEntity(
                                "가보호텔",
                                "경기도 평택시 평택5로76번길 18-10",
                                "http://tong.visitkorea.or.kr/cms/resource/83/1942883_image2_1.jpg",
                                "http://tong.visitkorea.or.kr/cms/resource/83/1942883_image3_1.jpg",
                                "경기도 평택시 평택5로76번길 18-10에 있는 가보호텔",
                                B02010100,
                                "0507-1396-7702",
                                36.9932008224,
                                127.1128582139,
                                List.of(
                                        new RoomEntity(
                                                null, 2, 116500L, 1
                                        ),
                                        new RoomEntity(
                                                null, 4, 223200L, 1
                                        )
                                )
                        )
                )));

        // When
        List<AccommodationResponse> accommodations = accommodationService.getAccommodationsByCategory(B02010100, PageRequest.of(0, 20));

        // Then
        assertThat(accommodations).hasSize(2);
        then(accommodationRepository).should().findByCategory(any(AccommodationCategory.class), any(Pageable.class));
    }

    @DisplayName("숙소 개별 조회 (상세 조회)")
    @Test
    void givenAccommodationId_whenSearchingAccommodationById_thenReturnsAccommodation() {
        // Given
        AccommodationEntity entity = new AccommodationEntity(
                "가경재 [한국관광 품질인증/Korea Quality]",
                "경상북도 안동시 하회남촌길 69-5",
                "http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg",
                "경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]",
                B02011600,
                "054-855-8552",
                36.537653745,
                128.5175868107,
                List.of(
                        new RoomEntity(
                                null, 2, 117500L, 1
                        ),
                        new RoomEntity(
                                null, 4, 213300L, 1
                        )
                )
        );
        long accommodationId = 1L;

        given(accommodationRepository.findById(accommodationId))
                .willReturn(
                        Optional.of(
                                entity
                        )
                );

        AccommodationResponse mock = Mockito.mock(AccommodationResponse.class);

        given(accommodationConverter.toResponse(entity))
                .willReturn(mock);

        // When
        accommodationService.getAccommodationById(accommodationId);

        // Then
        then(accommodationRepository).should().findById(accommodationId);
        then(accommodationConverter).should().toResponse(entity);
    }
}