package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true) @ToString
public class Event extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;
    @Column
    private String description;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false)
    private int reservedSeats;
    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}
