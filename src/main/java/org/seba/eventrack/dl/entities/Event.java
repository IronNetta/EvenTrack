package org.seba.eventrack.dl.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.dl.enums.EventType;
import org.seba.eventrack.dl.enums.TicketType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Schema(description = "Map of ticket type and corresponding price",
            example = "{\"STANDARD\": 50.0, \"VIP\": 100.0}")
    @ElementCollection
    @CollectionTable(name = "ticket_prices", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "ticket_type")
    @Column(name = "price")
    private Map<TicketType, Double> ticketPrices = new HashMap<>();

    @Column
    private Double ticketPrice;

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

    public Event(String title, String description, String location, int capacity, String imageUrl, Map<TicketType, Double> ticketPrices, EventType eventType) {
        this(title, description, location, capacity);
        this.imageUrl = imageUrl;
        this.eventType = eventType;
        this.ticketPrices = ticketPrices;
    }

    public Event(String title, String description, LocalDateTime date, String location, int capacity, EventType eventType) {
        this(title, description, location, capacity);
        this.date = date;
        this.eventType = eventType;
    }

    public Event(String title, String description, String location, int capacity) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.eventStatus = EventStatus.PENDING;
    }

    public Event(String title, String description, String location, int capacity, String imageUrl, Double ticketPrice, EventType eventType) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.imageUrl = imageUrl;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
        this.ticketPrices = new HashMap<>();
        this.ticketPrices.put(TicketType.STANDARD, ticketPrice);  // Ajout du prix standard à la map
    }

    public void setTicketPrice(TicketType ticketType, Double price) {
        if (ticketPrices == null) {
            ticketPrices = new HashMap<>();
        }
        ticketPrices.put(ticketType, price);
    }

    public Double getTicketPrice(TicketType ticketType) {
        return ticketPrices.get(ticketType);
    }

    public void setPrice(Double price) {
        this.ticketPrices.put(TicketType.STANDARD, price);
    }

    public Double getPrice() {
        return ticketPrices.get(TicketType.STANDARD);
    }
}