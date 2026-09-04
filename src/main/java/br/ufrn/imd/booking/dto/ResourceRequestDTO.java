package br.ufrn.imd.booking.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Dados de entrada para criação ou atualização de um recurso")
public record ResourceRequestDTO (
        @Schema(description = "Nome do recurso", example = "Sala de Reunião A", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "Descrição do recurso", example = "Sala com capacidade para 20 pessoas", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String description,

        @Schema(description = "Capacidade máxima de pessoas", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        Integer capacity,

        @Schema(description = "Preço por hora de uso do recurso", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        BigDecimal pricePerHour
){}
