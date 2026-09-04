package br.ufrn.imd.booking.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de resposta de um recurso")
public record ResourceResponseDTO(
        @Schema(description = "ID único do recurso")
        UUID id,

        @Schema(description = "Nome do recurso", example = "Sala de Reunião A")
        String name,

        @Schema(description = "Descrição do recurso", example = "Sala com capacidade para 20 pessoas")
        String description,

        @Schema(description = "Capacidade máxima de pessoas", example = "20")
        Integer capacity,

        @Schema(description = "Preço por hora de uso do recurso", example = "50.00")
        BigDecimal pricePerHour,

        @Schema(description = "Indica se o recurso está ativo", example = "true")
        boolean active
) {}
