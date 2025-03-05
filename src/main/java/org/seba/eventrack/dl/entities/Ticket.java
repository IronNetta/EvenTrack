package org.seba.eventrack.dl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.seba.eventrack.dl.entities.base.BaseEntity;
import org.seba.eventrack.dl.enums.TicketStatus;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true) @ToString
public class Ticket extends BaseEntity<Long> {

    @Column (unique = true)
    private String qrCodeUrl;

    @Column(nullable = false)
    private boolean isPaid;

    @Column
    private String paymentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.AVAILABLE;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User participant;

    public Ticket(User participant, Event event) {
        this.participant = participant;
        this.event = event;
    }
}