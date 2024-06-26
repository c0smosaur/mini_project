package db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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

    // 입력된 인원 수 검증
    public Boolean validateRoomCapacity(Integer capacity, RoomEntity roomEntity){
        if (capacity > 0) {
            return capacity <= roomEntity.getMaxCapacity();
        } else {
            return false;
        }
    }
}
