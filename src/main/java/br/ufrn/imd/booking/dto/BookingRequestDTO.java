package br.ufrn.imd.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRequestDTO(
        @NotNull
        UUID resourceId,

        @NotNull
        @Future
        LocalDateTime startDateTime,

        @NotNull
        @Future
        LocalDateTime endDateTime
) {}