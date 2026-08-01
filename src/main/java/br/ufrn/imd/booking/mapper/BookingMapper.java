package br.ufrn.imd.booking.mapper;


import br.ufrn.imd.booking.dto.BookingRequestDTO;
import br.ufrn.imd.booking.dto.BookingResponseDTO;
import br.ufrn.imd.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ResourceMapper.class})
public interface BookingMapper {
    BookingResponseDTO toResponseDTO(Booking booking);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "resource", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toEntity(BookingRequestDTO requestDTO);
}
