package api.controller;

import api.common.result.ResultWrapper;
import api.model.response.AccommodationDetailResponse;
import api.model.response.AccommodationResponse;
import api.service.AccommodationService;
import db.enums.AccommodationCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    // 숙소 카테고리별 조회
    @GetMapping()
    public ResponseEntity<ResultWrapper<List<AccommodationResponse>>> getAccommodationsByCategory(
            @RequestParam(required = false) AccommodationCategory category,
            final Pageable pageable
    ) {
        if (category == null) {
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(ResultWrapper.OK(accommodationService.getAllAccommodations(pageable)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(ResultWrapper.OK(accommodationService.getAccommodationsByCategory(category, pageable)));
        }
    }


    // 숙소 개별 조회 (상세 조회)
    @GetMapping("/{accommodationId}")
    public ResponseEntity<ResultWrapper<AccommodationDetailResponse>> getAccommodationById(
            @PathVariable Long accommodationId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(ResultWrapper.OK(accommodationService.getAccommodationById(accommodationId)));
    }
}
