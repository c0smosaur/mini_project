package db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity(name="product_detail")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class RoomEntity extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    private Integer maxCapacity;

    @Column(precision = 11, scale = 4)
    private BigDecimal price;

    private Integer stock;

}
