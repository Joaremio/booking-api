package br.ufrn.imd.booking.mapper;


import br.ufrn.imd.booking.dto.UserRequestDTO;
import br.ufrn.imd.booking.dto.UserResponseDTO;
import br.ufrn.imd.booking.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTO(User user);
    User toEntity(UserRequestDTO requestDTO);
}
