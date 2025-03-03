package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.dl.enums.EventType;

import java.time.LocalDateTime;
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

    @Column
    private Double price;

    @Column
    private LocalDateTime date;

    @Column
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public Event(String title, String description, LocalDateTime localDateTime, String location, int capacity) {
        this(title, description, location, capacity);
    }

    public Event(String title, String description, String location, int capacity) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.eventStatus = EventStatus.PENDING;
    }
}