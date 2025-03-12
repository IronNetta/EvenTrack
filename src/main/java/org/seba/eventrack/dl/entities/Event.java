package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.dl.enums.EventType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public Event(String title, String description, String location, int capacity, String imageUrl, Double price, EventType eventType) {
        this(title, description, location, capacity);
        this.imageUrl = imageUrl;
        this.eventType = eventType;
        this.price = price;
    }

    public Event(String title, String description, LocalDateTime date, String location, int capacity, EventType eventType) {
        this(title, description, location, capacity);
        this.date = date;
        this.eventType = eventType;
        this.price = 50.0;
    }

    public Event(String title, String description, String location, int capacity) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.eventStatus = EventStatus.PENDING;
    }
}