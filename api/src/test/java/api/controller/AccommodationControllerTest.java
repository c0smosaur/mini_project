package api.controller;

import api.config.auth.JwtAuthenticationFilter;
import api.model.response.AccommodationResponse;
import api.model.response.RoomResponse;
import api.service.AccommodationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static db.enums.AccommodationCategory.B02010100;
import static db.enums.AccommodationCategory.B02011600;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AccommodationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class AccommodationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @DisplayName("[GET] 숙소 전체 조회")
    @Test
    @WithMockUser
    void givenNothing_whenRequestingAllAccommodations_thenReturnsAccommodationsInStandardResponse() throws Exception {
        // Given
        given(accommodationService.getAllAccommodations(PageRequest.of(0, 20)))
                .willReturn(List.of(
                        AccommodationResponse.builder()
                                .id(1L)
                                .title("가경재 [한국관광 품질인증/Korea Quality]")
                                .address("경상북도 안동시 하회남촌길 69-5")
                                .image1("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg")
                                .image2("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg")
                                .description("경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]")
                                .category(B02011600)
                                .tel("054-855-8552")
                                .latitude(36.537653745)
                                .longitude(128.5175868107)
                                .rooms(List.of(
                                        RoomResponse.builder()
                                                .id(1L)
                                                .maxCapacity(2)
                                                .price(117500L)
                                                .stock(1)
                                                .build(),
                                        RoomResponse.builder()
                                                .id(2L)
                                                .maxCapacity(4)
                                                .price(213300L)
                                                .stock(1)
                                                .build()
                                ))
                                .build(),
                        AccommodationResponse.builder()
                                .id(2L)
                                .title("가락관광호텔")
                                .address("서울특별시 송파구 송파대로28길 5")
                                .image1("")
                                .image2("")
                                .description("서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔")
                                .category(B02010100)
                                .tel("02-400-6641~3")
                                .latitude(37.4966565128)
                                .longitude(127.1166298703)
                                .rooms(List.of(
                                        RoomResponse.builder()
                                                .id(3L)
                                                .maxCapacity(2)
                                                .price(110000L)
                                                .stock(1)
                                                .build(),
                                        RoomResponse.builder()
                                                .id(4L)
                                                .maxCapacity(4)
                                                .price(225000L)
                                                .stock(1)
                                                .build()
                                ))
                                .build()
                ));

        // When & Then
        mockMvc.perform(get("/api/accommodations").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.code").value(200))
                .andExpect(jsonPath("$.result.message").value("성공"))
                .andExpect(jsonPath("$.result.description").value("성공"))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body[0].id").value(1))
                .andExpect(jsonPath("$.body[0].title").value("가경재 [한국관광 품질인증/Korea Quality]"))
                .andExpect(jsonPath("$.body[0].address").value("경상북도 안동시 하회남촌길 69-5"))
                .andExpect(jsonPath("$.body[0].image1").value("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg"))
                .andExpect(jsonPath("$.body[0].image2").value("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg"))
                .andExpect(jsonPath("$.body[0].description").value("경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]"))
                .andExpect(jsonPath("$.body[0].category").value("B02011600"))
                .andExpect(jsonPath("$.body[0].tel").value("054-855-8552"))
                .andExpect(jsonPath("$.body[0].latitude").value(36.537653745))
                .andExpect(jsonPath("$.body[0].longitude").value(128.5175868107))
                .andExpect(jsonPath("$.body[0].rooms.[0].id").value(1))
                .andExpect(jsonPath("$.body[0].rooms.[0].maxCapacity").value(2))
                .andExpect(jsonPath("$.body[0].rooms.[0].price").value(117500))
                .andExpect(jsonPath("$.body[0].rooms.[0].stock").value(1))
                .andExpect(jsonPath("$.body[0].rooms.[1].id").value(2))
                .andExpect(jsonPath("$.body[0].rooms.[1].maxCapacity").value(4))
                .andExpect(jsonPath("$.body[0].rooms.[1].price").value(213300))
                .andExpect(jsonPath("$.body[0].rooms.[1].stock").value(1))
                .andExpect(jsonPath("$.body[1].id").value(2))
                .andExpect(jsonPath("$.body[1].title").value("가락관광호텔"))
                .andExpect(jsonPath("$.body[1].address").value("서울특별시 송파구 송파대로28길 5"))
                .andExpect(jsonPath("$.body[1].image1").value(""))
                .andExpect(jsonPath("$.body[1].image2").value(""))
                .andExpect(jsonPath("$.body[1].description").value("서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔"))
                .andExpect(jsonPath("$.body[1].category").value("B02010100"))
                .andExpect(jsonPath("$.body[1].tel").value("02-400-6641~3"))
                .andExpect(jsonPath("$.body[1].latitude").value(37.4966565128))
                .andExpect(jsonPath("$.body[1].longitude").value(127.1166298703))
                .andExpect(jsonPath("$.body[1].rooms.[0].id").value(3))
                .andExpect(jsonPath("$.body[1].rooms.[0].maxCapacity").value(2))
                .andExpect(jsonPath("$.body[1].rooms.[0].price").value(110000))
                .andExpect(jsonPath("$.body[1].rooms.[0].stock").value(1))
                .andExpect(jsonPath("$.body[1].rooms.[1].id").value(4))
                .andExpect(jsonPath("$.body[1].rooms.[1].maxCapacity").value(4))
                .andExpect(jsonPath("$.body[1].rooms.[1].price").value(225000))
                .andExpect(jsonPath("$.body[1].rooms.[1].stock").value(1));
    }

    @DisplayName("[GET] 숙소 카테고리별 조회")
    @Test
    @WithMockUser
    void givenAccommodationCategory_whenRequestingAccommodationsByCategory_thenReturnsAccommodationsInStandardResponse() throws Exception {
        // Given
        given(accommodationService.getAccommodationsByCategory(B02010100, PageRequest.of(0, 20)))
                .willReturn(List.of(
                        AccommodationResponse.builder()
                                .id(2L)
                                .title("가락관광호텔")
                                .address("서울특별시 송파구 송파대로28길 5")
                                .image1("")
                                .image2("")
                                .description("서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔")
                                .category(B02010100)
                                .tel("02-400-6641~3")
                                .latitude(37.4966565128)
                                .longitude(127.1166298703)
                                .rooms(List.of(
                                        RoomResponse.builder()
                                                .id(3L)
                                                .maxCapacity(2)
                                                .price(110000L)
                                                .stock(1)
                                                .build(),
                                        RoomResponse.builder()
                                                .id(4L)
                                                .maxCapacity(4)
                                                .price(225000L)
                                                .stock(1)
                                                .build()
                                ))
                                .build(),
                        AccommodationResponse.builder()
                                .id(10L)
                                .title("가보호텔")
                                .address("경기도 평택시 평택5로76번길 18-10")
                                .image1("http://tong.visitkorea.or.kr/cms/resource/83/1942883_image2_1.jpg")
                                .image2("http://tong.visitkorea.or.kr/cms/resource/83/1942883_image3_1.jpg")
                                .description("경기도 평택시 평택5로76번길 18-10에 있는 가보호텔")
                                .category(B02010100)
                                .tel("0507-1396-7702")
                                .latitude(36.9932008224)
                                .longitude(127.1128582139)
                                .rooms(List.of(
                                        RoomResponse.builder()
                                                .id(19L)
                                                .maxCapacity(2)
                                                .price(116500L)
                                                .stock(1)
                                                .build(),
                                        RoomResponse.builder()
                                                .id(20L)
                                                .maxCapacity(4)
                                                .price(223200L)
                                                .stock(1)
                                                .build()
                                ))
                                .build()
                ));

        // When & Then
        mockMvc.perform(get("/api/accommodations").contentType(contentType).param("category", String.valueOf(B02010100)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.code").value(200))
                .andExpect(jsonPath("$.result.message").value("성공"))
                .andExpect(jsonPath("$.result.description").value("성공"))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body[0].id").value(2))
                .andExpect(jsonPath("$.body[0].title").value("가락관광호텔"))
                .andExpect(jsonPath("$.body[0].address").value("서울특별시 송파구 송파대로28길 5"))
                .andExpect(jsonPath("$.body[0].image1").value(""))
                .andExpect(jsonPath("$.body[0].image2").value(""))
                .andExpect(jsonPath("$.body[0].description").value("서울특별시 송파구 송파대로28길 5에 있는 가락관광호텔"))
                .andExpect(jsonPath("$.body[0].category").value("B02010100"))
                .andExpect(jsonPath("$.body[0].tel").value("02-400-6641~3"))
                .andExpect(jsonPath("$.body[0].latitude").value(37.4966565128))
                .andExpect(jsonPath("$.body[0].longitude").value(127.1166298703))
                .andExpect(jsonPath("$.body[0].rooms.[0].id").value(3))
                .andExpect(jsonPath("$.body[0].rooms.[0].maxCapacity").value(2))
                .andExpect(jsonPath("$.body[0].rooms.[0].price").value(110000))
                .andExpect(jsonPath("$.body[0].rooms.[0].stock").value(1))
                .andExpect(jsonPath("$.body[0].rooms.[1].id").value(4))
                .andExpect(jsonPath("$.body[0].rooms.[1].maxCapacity").value(4))
                .andExpect(jsonPath("$.body[0].rooms.[1].price").value(225000))
                .andExpect(jsonPath("$.body[0].rooms.[1].stock").value(1))
                .andExpect(jsonPath("$.body[1].id").value(10))
                .andExpect(jsonPath("$.body[1].title").value("가보호텔"))
                .andExpect(jsonPath("$.body[1].address").value("경기도 평택시 평택5로76번길 18-10"))
                .andExpect(jsonPath("$.body[1].image1").value("http://tong.visitkorea.or.kr/cms/resource/83/1942883_image2_1.jpg"))
                .andExpect(jsonPath("$.body[1].image2").value("http://tong.visitkorea.or.kr/cms/resource/83/1942883_image3_1.jpg"))
                .andExpect(jsonPath("$.body[1].description").value("경기도 평택시 평택5로76번길 18-10에 있는 가보호텔"))
                .andExpect(jsonPath("$.body[1].category").value("B02010100"))
                .andExpect(jsonPath("$.body[1].tel").value("0507-1396-7702"))
                .andExpect(jsonPath("$.body[1].latitude").value(36.9932008224))
                .andExpect(jsonPath("$.body[1].longitude").value(127.1128582139))
                .andExpect(jsonPath("$.body[1].rooms.[0].id").value(19))
                .andExpect(jsonPath("$.body[1].rooms.[0].maxCapacity").value(2))
                .andExpect(jsonPath("$.body[1].rooms.[0].price").value(116500))
                .andExpect(jsonPath("$.body[1].rooms.[0].stock").value(1))
                .andExpect(jsonPath("$.body[1].rooms.[1].id").value(20))
                .andExpect(jsonPath("$.body[1].rooms.[1].maxCapacity").value(4))
                .andExpect(jsonPath("$.body[1].rooms.[1].price").value(223200))
                .andExpect(jsonPath("$.body[1].rooms.[1].stock").value(1));
    }

    @DisplayName("[GET] 숙소 개별 조회 (상세 조회)")
    @Test
    @WithMockUser
    void givenAccommodationId_whenRequestingAccommodationById_thenReturnsAccommodationInStandardResponse() throws Exception {
        // Given
        given(accommodationService.getAccommodationById(1L))
                .willReturn(AccommodationResponse.builder()
                        .id(1L)
                        .title("가경재 [한국관광 품질인증/Korea Quality]")
                        .address("경상북도 안동시 하회남촌길 69-5")
                        .image1("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg")
                        .image2("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg")
                        .description("경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]")
                        .category(B02011600)
                        .tel("054-855-8552")
                        .latitude(36.537653745)
                        .longitude(128.5175868107)
                        .rooms(List.of(
                                RoomResponse.builder()
                                        .id(1L)
                                        .maxCapacity(2)
                                        .price(117500L)
                                        .stock(1)
                                        .build(),
                                RoomResponse.builder()
                                        .id(2L)
                                        .maxCapacity(4)
                                        .price(213300L)
                                        .stock(1)
                                        .build()
                        ))
                        .build()
                );

        // When & Then
        mockMvc.perform(get("/api/accommodations/{accommodationId}", 1).contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.code").value(200))
                .andExpect(jsonPath("$.result.message").value("성공"))
                .andExpect(jsonPath("$.result.description").value("성공"))
                .andExpect(jsonPath("$.body.id").value(1))
                .andExpect(jsonPath("$.body.title").value("가경재 [한국관광 품질인증/Korea Quality]"))
                .andExpect(jsonPath("$.body.address").value("경상북도 안동시 하회남촌길 69-5"))
                .andExpect(jsonPath("$.body.image1").value("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image2_1.jpg"))
                .andExpect(jsonPath("$.body.image2").value("http://tong.visitkorea.or.kr/cms/resource/00/2626200_image3_1.jpg"))
                .andExpect(jsonPath("$.body.description").value("경상북도 안동시 하회남촌길 69-5에 있는 가경재 [한국관광 품질인증/Korea Quality]"))
                .andExpect(jsonPath("$.body.category").value("B02011600"))
                .andExpect(jsonPath("$.body.tel").value("054-855-8552"))
                .andExpect(jsonPath("$.body.latitude").value(36.537653745))
                .andExpect(jsonPath("$.body.longitude").value(128.5175868107))
                .andExpect(jsonPath("$.body.rooms.[0].id").value(1))
                .andExpect(jsonPath("$.body.rooms.[0].maxCapacity").value(2))
                .andExpect(jsonPath("$.body.rooms.[0].price").value(117500))
                .andExpect(jsonPath("$.body.rooms.[0].stock").value(1))
                .andExpect(jsonPath("$.body.rooms.[1].id").value(2))
                .andExpect(jsonPath("$.body.rooms.[1].maxCapacity").value(4))
                .andExpect(jsonPath("$.body.rooms.[1].price").value(213300))
                .andExpect(jsonPath("$.body.rooms.[1].stock").value(1));
    }
}