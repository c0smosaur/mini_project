package db.entity;

import db.enums.CartStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="cart")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CartEntity extends BaseEntity {
    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long roomId;

    private Integer capacity;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private CartStatus status;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime modifiedAt;
}