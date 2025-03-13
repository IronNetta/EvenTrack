package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.BadgeType;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Badge extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private BadgeType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
