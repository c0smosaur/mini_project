package api.controller;

import api.common.result.ResultWrapper;
import api.model.response.AccommodationResponse;
import api.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<ResultWrapper<List<AccommodationResponse>>> getAllAccommodations(final Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(accommodationService.getAllAccommodations(pageable)));
    }
}
