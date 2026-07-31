package br.ufrn.imd.booking.dto;

import br.ufrn.imd.booking.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
