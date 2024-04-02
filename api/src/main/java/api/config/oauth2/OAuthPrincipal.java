package api.config.oauth2;

import db.entity.MemberEntity;
import db.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthPrincipal implements OAuth2User, UserDetails {

    private String username;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public static OAuthPrincipal create(Map<String, Object> attributes){
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("" + MemberType.USER));
        return OAuthPrincipal.builder()
                .username((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .authorities(authorities)
                .attributes(attributes)
                .build();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return String.valueOf(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
