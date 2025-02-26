package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @Table(name = "user_")
@ToString(of = {"email","role"}) @EqualsAndHashCode(callSuper = true, of = {"email"})
public class User extends BaseEntity<Long> implements UserDetails {

    @Column(unique = true, nullable = false, length = 50)
    private String username;
    @Column(unique = true, nullable = false, length = 250)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> organizedEvents;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
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
}
