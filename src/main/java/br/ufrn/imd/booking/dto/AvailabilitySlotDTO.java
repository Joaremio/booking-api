package br.ufrn.imd.booking.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilitySlotDTO(
        UUID bookingId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {}