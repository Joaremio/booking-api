package br.ufrn.imd.booking.service;


import br.ufrn.imd.booking.dto.BookingRequestDTO;
import br.ufrn.imd.booking.dto.BookingResponseDTO;
import br.ufrn.imd.booking.dto.ResourceResponseDTO;
import br.ufrn.imd.booking.dto.UserResponseDTO;
import br.ufrn.imd.booking.entity.Booking;
import br.ufrn.imd.booking.entity.Resource;
import br.ufrn.imd.booking.entity.User;
import br.ufrn.imd.booking.enums.Role;
import br.ufrn.imd.booking.enums.Status;
import br.ufrn.imd.booking.mapper.BookingMapper;
import br.ufrn.imd.booking.repository.BookingRepository;
import br.ufrn.imd.booking.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingService bookingService;

    private UUID resourceId = UUID.randomUUID();

    private User user = User.builder()
            .id(UUID.randomUUID()).name("Pedro").email("pedro@gmail.com").password("123456").role(Role.USER).build();

    private User admin = User.builder()
            .id(UUID.randomUUID())
            .name("Admin")
            .email("admin@gmail.com")
            .password("123456")
            .role(Role.ADMIN)
            .build();

    private Resource resource = Resource.builder()
            .id(resourceId).name("Quadra").description("Quadra de tênis").capacity(20).pricePerHour(BigDecimal.TEN).build();

    private BookingRequestDTO data = new BookingRequestDTO(
            resourceId,
            LocalDateTime.of(2026, 9, 1, 10, 0),
            LocalDateTime.of(2026, 9, 1, 11, 0)
    );

    private UserResponseDTO userResponseDTO = new UserResponseDTO(
            user.getId(), user.getName(), user.getEmail(), user.getRole(), null
    );

    private ResourceResponseDTO resourceResponseDTO = new ResourceResponseDTO(
            resource.getId(), resource.getName(), resource.getDescription(),
            resource.getCapacity(), resource.getPricePerHour(), true
    );

    private Booking booking = Booking.builder().id(UUID.randomUUID()).user(user).resource(resource).startDateTime(data.startDateTime()).endDateTime(data.endDateTime()).status(Status.APROVADO).createdAt(null).updatedAt(null).version(null).build();


    @Test
    void createBooking (){

        Booking savedBooking = Booking.builder()
                .id(UUID.randomUUID())
                .user(user)
                .resource(resource)
                .startDateTime(data.startDateTime())
                .endDateTime(data.endDateTime())
                .status(Status.APROVADO)
                .build();

        BookingResponseDTO response = new BookingResponseDTO(
                savedBooking.getId(),
                userResponseDTO,
                resourceResponseDTO,
                data.startDateTime(),
                data.endDateTime(),
                Status.APROVADO,
                null
        );

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(bookingRepository.existsOverlappingBooking(resourceId, data.startDateTime(), data.endDateTime()))
                .thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toResponseDTO(savedBooking)).thenReturn(response);

        BookingResponseDTO result = bookingService.createBooking(data, user);
        assertEquals(response, result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBookingResourceNotFound(){
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.createBooking(data, user));
    }

    @Test
    void createBookingConflict(){
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(bookingRepository.existsOverlappingBooking(resourceId, data.startDateTime(), data.endDateTime())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(data, user));
    }

    @Test
    void getBookingById(){

        BookingResponseDTO response = new BookingResponseDTO(
                booking.getId(),
                userResponseDTO,
                resourceResponseDTO,
                data.startDateTime(),
                data.endDateTime(),
                Status.APROVADO,
                null
        );


        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.toResponseDTO(booking)).thenReturn(response);

        BookingResponseDTO result =  bookingService.getBookingById(booking.getId(), user);

        assertEquals(response, result);
        verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void getBookingById_quandoAdmin_retornaBookingDeOutroUsuario(){

        BookingResponseDTO response = new BookingResponseDTO(
                booking.getId(),
                userResponseDTO,
                resourceResponseDTO,
                data.startDateTime(),
                data.endDateTime(),
                Status.APROVADO,
                null
        );

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.toResponseDTO(booking)).thenReturn(response);

        BookingResponseDTO result =  bookingService.getBookingById(booking.getId(), admin);

        assertEquals(response, result);
        verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void getBookingById_quandoUsuarioComum_tentaAcessarBookingDeOutro() {
        User outroUser = User.builder()
                .id(UUID.randomUUID())
                .name("Lucas")
                .email("lucas@gmail.com")
                .password("123456")
                .role(Role.USER)
                .build();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.getBookingById(booking.getId(), outroUser));
    }
}
