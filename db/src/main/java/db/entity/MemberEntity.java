package db.entity;

import db.enums.MemberStatus;
import db.enums.MemberType;
import db.enums.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name="member")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity extends BaseEntity{

    // email
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = true, length = 100)
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

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Embedded
    private SocialOAuth socialOAuth;
}
