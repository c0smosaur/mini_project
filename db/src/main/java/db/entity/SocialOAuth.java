package db.entity;

import db.enums.OAuthProvider;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class SocialOAuth {
    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oAuthProvider;
    private String oAuthId;
    private String email;
    private String name;
    private String attributes;
}
