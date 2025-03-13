package org.seba.eventrack.dl.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.seba.eventrack.il.serializer.TicketTypeDeserializer;

public enum TicketType {
    VIP,
    STANDARD,
    YOUNG;

    @JsonDeserialize(using = TicketTypeDeserializer.class)
    public static TicketType fromString(String value) {
        return TicketType.valueOf(value.toUpperCase());
    }
}
