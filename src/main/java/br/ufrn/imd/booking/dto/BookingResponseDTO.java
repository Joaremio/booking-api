package br.ufrn.imd.booking.dto;

import br.ufrn.imd.booking.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de resposta de um agendamento")
public record BookingResponseDTO(
        @Schema(description = "ID único do agendamento")
        UUID id,

        @Schema(description = "Dados do usuário que realizou o agendamento")
        UserResponseDTO user,

        @Schema(description = "Dados do recurso agendado")
        ResourceResponseDTO resource,

        @Schema(description = "Data e hora de início do agendamento", example = "2026-09-10T14:00:00")
        LocalDateTime startDateTime,

        @Schema(description = "Data e hora de término do agendamento", example = "2026-09-10T16:00:00")
        LocalDateTime endDateTime,

        @Schema(description = "Status atual do agendamento", example = "PENDENTE")
        Status status,

        @Schema(description = "Data e hora de criação do registro")
        LocalDateTime createdAt
) {}
