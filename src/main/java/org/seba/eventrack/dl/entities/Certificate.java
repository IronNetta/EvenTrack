package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Certificate extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();
}