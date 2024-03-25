package api.service;

import api.converter.AccommodationConverter;
import api.model.response.AccommodationResponse;
import db.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAllAccommodations() {
        return accommodationRepository.findAll()
                .stream()
                .map(accommodationConverter::toResponse)
                .toList();
    }
}