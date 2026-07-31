package br.ufrn.imd.booking.dto;
import java.math.BigDecimal;
import java.util.UUID;

public record ResourceResponseDTO(
        UUID id,
        String name,
        String description,
        Integer capacity,
        BigDecimal pricePerHour,
        boolean active
) {}
