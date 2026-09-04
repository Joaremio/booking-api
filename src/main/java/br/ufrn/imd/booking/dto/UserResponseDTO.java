package br.ufrn.imd.booking.dto;

import br.ufrn.imd.booking.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de resposta de um usuário")
public record UserResponseDTO(
        @Schema(description = "ID único do usuário")
        UUID id,

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String name,

        @Schema(description = "E-mail do usuário", example = "joao@email.com")
        String email,

        @Schema(description = "Papel do usuário no sistema", example = "USER")
        Role role,

        @Schema(description = "Data e hora de criação da conta")
        LocalDateTime createdAt
) {
}
