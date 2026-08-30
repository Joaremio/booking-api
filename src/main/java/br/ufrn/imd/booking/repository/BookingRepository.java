package br.ufrn.imd.booking.repository;

import br.ufrn.imd.booking.entity.Booking;
import br.ufrn.imd.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.resource.id = :resourceId
        AND b.status != 'CANCELLED'
        AND b.startDateTime < :endDateTime
        AND b.endDateTime > :startDateTime
    """)
    boolean existsOverlappingBooking(
            @Param("resourceId") UUID resourceId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
    List<Booking> findAllBookingsByUserId( UUID userId);
}