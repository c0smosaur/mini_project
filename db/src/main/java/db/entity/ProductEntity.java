package db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends BaseEntity{

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

}
