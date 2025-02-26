package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.NotificationPreference;
import org.seba.eventrack.dl.enums.PaymentMethod;
import org.seba.eventrack.dl.enums.UserRole;
import org.seba.eventrack.dl.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "user_")
@ToString(of = {"email","role"}) @EqualsAndHashCode(callSuper = true, of = {"email"})
public class User extends BaseEntity<Long> implements UserDetails {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 250)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationPreference notificationPreferences = NotificationPreference.EMAIL;

    @Column
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Column
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod preferredPaymentMethod = PaymentMethod.NONE;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> organizedEvents;

    public User() {
        this.organizedEvents = new ArrayList<>();
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.PARTICIPANT;
    }

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
        return this.status == UserStatus.ACTIVE;
    }
}
