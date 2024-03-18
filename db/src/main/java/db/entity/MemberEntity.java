package db.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name="member")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberEntity extends BaseEntity{


}
