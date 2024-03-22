package api.service;

import api.converter.AccommodationConverter;
import api.model.response.AccommodationResponse;
import db.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;

    public List<AccommodationResponse> getAllAccommodations() {
        return accommodationRepository.findAll()
                .stream()
                .map(accommodationConverter::toResponse)
                .toList();
    }
}
