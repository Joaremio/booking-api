package br.ufrn.imd.booking.service;

import br.ufrn.imd.booking.dto.ResourceRequestDTO;
import br.ufrn.imd.booking.dto.ResourceResponseDTO;
import br.ufrn.imd.booking.entity.Resource;
import br.ufrn.imd.booking.mapper.ResourceMapper;
import br.ufrn.imd.booking.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceMapper resourceMapper;

    @InjectMocks
    private ResourceService resourceService;


    UUID id =  UUID.randomUUID();
    ResourceRequestDTO data = new ResourceRequestDTO("Quadra de futebol", "Quadra society com grama sintética, iluminação noturna", 10, BigDecimal.ONE);
    ResourceResponseDTO response = new ResourceResponseDTO(id, "Quadra de futebol", "Quadra society com grama sintética, iluminação noturna", 10, BigDecimal.ONE, true);


    @Test
    void createResource() {
       Resource resourceToSave = Resource.builder()
               .name(data.name())
               .description(data.description())
               .capacity(data.capacity())
               .pricePerHour(data.pricePerHour())
               .build();

       Resource savedResource = Resource.builder()
                .id(id)
                .name(data.name())
                .description(data.description())
                .capacity(data.capacity())
                .pricePerHour(data.pricePerHour())
                .active(true)
                .build();

       when(resourceMapper.toEntity(data)).thenReturn(resourceToSave);
       when(resourceRepository.save(resourceToSave)).thenReturn(savedResource);
       when(resourceMapper.toResponseDTO(savedResource)).thenReturn(response);

       ResourceResponseDTO result = resourceService.createResource(data);


        assertEquals(response, result);
        verify(resourceRepository).save(resourceToSave);
    }

    @Test
    void getResourceById(){

        Resource findResource = Resource.builder()
                .id(id)
                .name(data.name())
                .description(data.description())
                .capacity(data.capacity())
                .pricePerHour(data.pricePerHour())
                .active(true)
                .build();

        when(resourceRepository.findById(id)).thenReturn(Optional.of(findResource));
        when (resourceMapper.toResponseDTO(findResource)).thenReturn(response);

        ResourceResponseDTO result = resourceService.getResourceById(id);

        assertEquals(response, result);
    }

    @Test
    void getResourceById_quandoIdNaoExiste_lancaException() {
        UUID idInexistente = UUID.randomUUID();

        when(resourceRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            resourceService.getResourceById(idInexistente);
        });
    }

    @Test
    void deleteResourceById() {

        Resource findResource = Resource.builder()
                .id(id)
                .name(data.name())
                .description(data.description())
                .capacity(data.capacity())
                .pricePerHour(data.pricePerHour())
                .active(true)
                .build();

        when(resourceRepository.findById(id)).thenReturn(Optional.of(findResource));

        resourceService.deleteResource(id);

        assertFalse(findResource.isActive());
        verify(resourceRepository).save(findResource);
    }


    @Test
    void deleteResourceById_quandoIdNaoExiste_lancaException() {
        UUID idInexistente = UUID.randomUUID();

        when(resourceRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {resourceService.deleteResource(idInexistente);});
    }

    @Test
    void updateResource() {

        Resource findResource = Resource.builder()
                .id(id)
                .name("Nome Antigo")
                .description("Descrição antiga")
                .capacity(5)
                .pricePerHour(BigDecimal.TEN)
                .active(true)
                .build();



        when(resourceRepository.findById(id)).thenReturn(Optional.of(findResource));
        when(resourceRepository.save(findResource)).thenReturn(findResource);
        when(resourceMapper.toResponseDTO(findResource)).thenReturn(response);

        ResourceResponseDTO result = resourceService.updateResource(id, data);

        assertEquals(response, result);
        assertEquals(data.name(), findResource.getName());
        assertEquals(data.capacity(), findResource.getCapacity());
    }

    @Test
    void updateResource_quandoIdNaoExiste_lancaException() {
        UUID idInexistente = UUID.randomUUID();

        when(resourceRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            resourceService.updateResource(idInexistente, data);
        });
    }


}