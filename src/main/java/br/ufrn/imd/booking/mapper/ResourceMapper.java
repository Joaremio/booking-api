package br.ufrn.imd.booking.mapper;
import br.ufrn.imd.booking.dto.ResourceRequestDTO;
import br.ufrn.imd.booking.dto.ResourceResponseDTO;
import br.ufrn.imd.booking.entity.Resource;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface ResourceMapper {
    ResourceResponseDTO toResponseDTO(Resource resource);
    Resource toEntity (ResourceRequestDTO requestDTO);
}
