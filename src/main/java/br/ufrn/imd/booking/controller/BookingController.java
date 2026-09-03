package br.ufrn.imd.booking.controller;


import br.ufrn.imd.booking.dto.AvailabilitySlotDTO;
import br.ufrn.imd.booking.dto.BookingRequestDTO;
import br.ufrn.imd.booking.dto.BookingResponseDTO;
import br.ufrn.imd.booking.entity.User;
import br.ufrn.imd.booking.service.BookingService;
import jakarta.validation.Valid;
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
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO data,
            @AuthenticationPrincipal User user
    ){
        BookingResponseDTO booking = bookingService.createBooking(data, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(bookingService.getBookingsByUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(bookingService.getBookingById(id, user));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        bookingService.cancelBooking(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(
            @RequestParam UUID resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(bookingService.getAvailability(resourceId, date));
    }
}
