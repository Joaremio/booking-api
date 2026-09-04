package br.ufrn.imd.booking.controller;


import br.ufrn.imd.booking.dto.AvailabilitySlotDTO;
import br.ufrn.imd.booking.dto.BookingRequestDTO;
import br.ufrn.imd.booking.dto.BookingResponseDTO;
import br.ufrn.imd.booking.entity.User;
import br.ufrn.imd.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Agendamentos", description = "Gestão de agendamentos de recursos")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento para o usuário autenticado.")
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou conflito de horário")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO data,
            @AuthenticationPrincipal User user
    ){
        BookingResponseDTO booking = bookingService.createBooking(data, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/me")
    @Operation(summary = "Listar meus agendamentos", description = "Retorna todos os agendamentos do usuário autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(bookingService.getBookingsByUser(user.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna um agendamento específico. Apenas o proprietário ou um ADMIN pode acessar.")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @Parameter(description = "ID do agendamento") @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(bookingService.getBookingById(id, user));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento existente. Apenas o proprietário ou um ADMIN pode cancelar.")
    @ApiResponse(responseCode = "204", description = "Agendamento cancelado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<Void> cancelBooking(
            @Parameter(description = "ID do agendamento") @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        bookingService.cancelBooking(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    @Operation(summary = "Verificar disponibilidade", description = "Retorna os horários ocupados de um recurso em uma data específica.")
    @ApiResponse(responseCode = "200", description = "Lista de slots ocupados retornada")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(
            @Parameter(description = "ID do recurso") @RequestParam UUID resourceId,
            @Parameter(description = "Data no formato ISO (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(bookingService.getAvailability(resourceId, date));
    }
}
