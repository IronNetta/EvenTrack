package org.seba.eventrack.api.models.ticket.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record TicketPriceDTO(
        @Schema(example = "50.0", description = "Standard ticket price")
        Double STANDARD,

        @Schema(example = "100.0", description = "VIP ticket price")
        Double VIP,

        @Schema(example = "30.0", description = "Youth ticket price")
        Double YOUNG
) {}
