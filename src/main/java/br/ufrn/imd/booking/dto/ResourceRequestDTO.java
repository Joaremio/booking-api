package br.ufrn.imd.booking.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
public record ResourceRequestDTO (
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 255)
        String description,

        @NotNull
        @Positive
        Integer capacity,

        @NotNull
        @Positive
        BigDecimal pricePerHour
){}
