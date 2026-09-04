package br.ufrn.imd.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT de autenticação")
public record TokenResponseDTO(
        @Schema(description = "Token JWT para autenticação nas requisições", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String type
) {}
