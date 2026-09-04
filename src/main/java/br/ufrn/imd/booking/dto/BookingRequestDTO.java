package br.ufrn.imd.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de entrada para criação de um agendamento")
public record BookingRequestDTO(
        @Schema(description = "ID do recurso a ser agendado", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        UUID resourceId,

        @Schema(description = "Data e hora de início do agendamento (deve ser no futuro)", example = "2026-09-10T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Future
        LocalDateTime startDateTime,

        @Schema(description = "Data e hora de término do agendamento (deve ser no futuro)", example = "2026-09-10T16:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Future
        LocalDateTime endDateTime
) {}
