package br.ufrn.imd.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de entrada para autenticação")
public record LoginRequestDTO(
        @Schema(description = "E-mail do usuário", example = "usuario@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email String email,

        @Schema(description = "Senha do usuário (mínimo 7 caracteres)", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 7) String password
){}
