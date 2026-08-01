package br.ufrn.imd.booking.dto;

public record TokenResponseDTO(
        String token,
        String type
) {}