package db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name="room")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class RoomEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private AccommodationEntity accommodation;

    private Integer maxCapacity;

    @Column(precision = 11, scale = 4)
    private Long price;

    private Integer stock;

}
