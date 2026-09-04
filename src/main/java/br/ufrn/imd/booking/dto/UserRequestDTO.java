package br.ufrn.imd.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de entrada para registro de novo usuário")
public record UserRequestDTO(
        @Schema(description = "Nome completo do usuário", example = "João da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String name,

        @Schema(description = "E-mail do usuário (deve ser único)", example = "joao@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email
        @Size(max=255)
        String email,

        @Schema(description = "Senha do usuário (8 a 255 caracteres)", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min=8, max=255)
        String password
) {
}
