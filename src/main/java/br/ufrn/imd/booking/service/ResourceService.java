package br.ufrn.imd.booking.service;

import br.ufrn.imd.booking.dto.ResourceRequestDTO;
import br.ufrn.imd.booking.dto.ResourceResponseDTO;
import br.ufrn.imd.booking.entity.Resource;
import br.ufrn.imd.booking.mapper.ResourceMapper;
import br.ufrn.imd.booking.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public ResourceService (ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public ResourceResponseDTO createResource(ResourceRequestDTO data) {
        Resource resource = resourceMapper.toEntity(data);
        Resource savedResource = resourceRepository.save(resource);
        return resourceMapper.toResponseDTO(savedResource);
    }

    public List<ResourceResponseDTO> getAllResources() {
        List<Resource> resources = resourceRepository.findAll();

        return resources.stream().map(resourceMapper :: toResponseDTO).toList();
    }

    public ResourceResponseDTO getResourceById(UUID id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Resource not found"));

        return resourceMapper.toResponseDTO(resource);
    }

    public ResourceResponseDTO updateResource(UUID id, ResourceRequestDTO data) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + id));

        resource.setName(data.name());
        resource.setDescription(data.description());
        resource.setCapacity(data.capacity());
        resource.setPricePerHour(data.pricePerHour());

        Resource updatedResource = resourceRepository.save(resource);

        return resourceMapper.toResponseDTO(updatedResource);
    }

    public void deleteResource(UUID id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + id));

        resource.setActive(false);
        resourceRepository.save(resource);
    }
}
