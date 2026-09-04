package br.ufrn.imd.booking.service;

import br.ufrn.imd.booking.dto.AvailabilitySlotDTO;
import br.ufrn.imd.booking.dto.BookingRequestDTO;
import br.ufrn.imd.booking.dto.BookingResponseDTO;
import br.ufrn.imd.booking.entity.Booking;
import br.ufrn.imd.booking.entity.Resource;
import br.ufrn.imd.booking.entity.User;
import br.ufrn.imd.booking.enums.Role;
import br.ufrn.imd.booking.enums.Status;
import br.ufrn.imd.booking.mapper.BookingMapper;
import br.ufrn.imd.booking.repository.BookingRepository;
import br.ufrn.imd.booking.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final BookingMapper bookingMapper;

    public BookingResponseDTO createBooking(BookingRequestDTO data, User user) {

        Resource resource = resourceRepository.findById(data.resourceId()).orElseThrow(()->new EntityNotFoundException("Resource not found"));

        boolean hasConflict = bookingRepository.existsOverlappingBooking(
                resource.getId(), data.startDateTime(), data.endDateTime()
        );

        if(hasConflict){
            throw new IllegalArgumentException("There is already a booking for this resource in this time slot.");
        }

        Booking booking = Booking.builder()
                .user(user)
                .resource(resource)
                .startDateTime(data.startDateTime())
                .endDateTime(data.endDateTime())
                .status(Status.APROVADO)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponseDTO(savedBooking);
    }

    public List<AvailabilitySlotDTO> getAvailability(UUID resourceId, LocalDate date) {

        resourceRepository.findById(resourceId).orElseThrow(()->new EntityNotFoundException("Resource not found"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Booking> bookings = bookingRepository.findBookingsByResourceAndDateRange(
                resourceId, startOfDay, endOfDay
        );

        return bookings.stream()
                .map(booking -> new AvailabilitySlotDTO(
                        booking.getId(),
                        booking.getStartDateTime(),
                        booking.getEndDateTime()
                ))
                .toList();
    }

    public List<BookingResponseDTO> getBookingsByUser(UUID userId) {
        List<Booking> bookings = bookingRepository.findAllBookingsByUserId(userId);
        return bookings.stream().map( bookingMapper :: toResponseDTO ).toList();
    }

    public BookingResponseDTO getBookingById(UUID bookingId, User requester) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->new EntityNotFoundException("Booking not found"));

        boolean isOwner = booking.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole() ==  Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to view this booking.");
        }

        return bookingMapper.toResponseDTO(booking);
    }

    public void cancelBooking(UUID bookingId, User requester) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        boolean isOwner = booking.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to cancel this booking.");
        }

        booking.setStatus(Status.CANCELADO);
        bookingRepository.save(booking);
    }

}
