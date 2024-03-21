package db.entity;

import db.enums.MemberStatus;
import db.enums.MemberType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name="member")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberEntity extends BaseEntity{

    // email
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false, length = 45)
    @Enumerated(value = EnumType.STRING)
    private MemberType type;

    private String profileImage;
}
