package br.ufrn.imd.booking.dto;

import br.ufrn.imd.booking.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponseDTO(
        UUID id,
        UserResponseDTO user,
        ResourceResponseDTO resource,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Status status,
        LocalDateTime createdAt
) {}