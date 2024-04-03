package db.entity;

import db.enums.AccommodationCategory;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "accommodation")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class AccommodationEntity extends BaseEntity{

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 200)
    private String image1;

    @Column(length = 200)
    private String image2;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AccommodationCategory category;

    @Column(length = 50)
    private String tel;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "accommodation")
    private List<RoomEntity> rooms = List.of();
}
