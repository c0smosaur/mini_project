package api.service;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.converter.AccommodationConverter;
import api.converter.RoomConverter;
import api.model.response.AccommodationDetailResponse;
import api.model.response.AccommodationResponse;
import api.model.response.RoomResponse;
import db.entity.AccommodationEntity;
import db.enums.AccommodationCategory;
import db.repository.AccommodationRepository;
import db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;

    private final RoomRepository roomRepository;
    private final RoomConverter roomConverter;

    // 숙소 전체 조회
    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAllAccommodations(Pageable pageable) {
        Page<AccommodationResponse> page = accommodationRepository.findAll(pageable)
                .map(accommodationConverter::toResponse);

        return page.getContent();
    }

    // 숙소 카테고리별 조회
    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAccommodationsByCategory(AccommodationCategory category, Pageable pageable) {
        Page<AccommodationResponse> page = accommodationRepository.findByCategory(category, pageable)
                .map(accommodationConverter::toResponse);

        return page.getContent();
    }

    // 숙소 개별 조회 (상세 조회)
    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationById(Long id) {
        var optional = accommodationRepository.findById(id);
        AccommodationEntity accommodation = optional
                .orElseThrow(
                        () -> new ResultException(GeneralErrorCode.NOT_FOUND)
                );

        return AccommodationDetailResponse.builder()
                .accommodation(accommodationConverter.toResponse(accommodation))
                .rooms(getAllRoomsByAccommodationId(id))
                .build();
    }

    // 숙소 id에 해당하는 방 전체 조회
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRoomsByAccommodationId(Long accommodationId) {
        return roomRepository.findAllByAccommodationId(accommodationId)
                .stream()
                .map(roomConverter::toResponse)
                .toList();
    }
}