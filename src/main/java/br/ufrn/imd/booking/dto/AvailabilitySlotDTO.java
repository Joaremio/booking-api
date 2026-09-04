package br.ufrn.imd.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Slot de horário ocupado por um agendamento")
public record AvailabilitySlotDTO(
        @Schema(description = "ID do agendamento que ocupa este horário")
        UUID bookingId,

        @Schema(description = "Data e hora de início do agendamento", example = "2026-09-10T14:00:00")
        LocalDateTime startDateTime,

        @Schema(description = "Data e hora de término do agendamento", example = "2026-09-10T16:00:00")
        LocalDateTime endDateTime
) {}
