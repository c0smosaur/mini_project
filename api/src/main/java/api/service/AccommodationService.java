package api.service;

import api.converter.AccommodationConverter;
import api.model.response.AccommodationResponse;
import db.enums.AccommodationCategory;
import db.repository.AccommodationRepository;
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

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAllAccommodations(Pageable pageable) {
        Page<AccommodationResponse> page = accommodationRepository.findAll(pageable)
                .map(accommodationConverter::toResponse);
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAccommodationsByCategory(AccommodationCategory category, Pageable pageable) {
        Page<AccommodationResponse> page = accommodationRepository.findByCategory(category, pageable)
                .map(accommodationConverter::toResponse);
        return page.getContent();
    }
}