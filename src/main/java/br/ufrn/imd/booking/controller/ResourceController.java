package br.ufrn.imd.booking.controller;


import br.ufrn.imd.booking.dto.ResourceRequestDTO;
import br.ufrn.imd.booking.dto.ResourceResponseDTO;
import br.ufrn.imd.booking.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "Recursos", description = "Gestão de recursos (salas, equipamentos, etc)")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar recurso", description = "Cria um novo recurso. Apenas administradores.")
    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado — requer papel ADMIN")
    public ResponseEntity<ResourceResponseDTO> createResource (@Valid @RequestBody ResourceRequestDTO data) {
        ResourceResponseDTO resource = resourceService.createResource(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recurso por ID", description = "Retorna um recurso específico.")
    @ApiResponse(responseCode = "200", description = "Recurso encontrado")
    @ApiResponse(responseCode = "404", description = "Recurso não encontrado")
    public ResponseEntity<ResourceResponseDTO> getResource(
            @Parameter(description = "ID do recurso") @PathVariable UUID id){
        ResourceResponseDTO resource = resourceService.getResourceById(id);
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os recursos", description = "Retorna a lista de todos os recursos ativos.")
    @ApiResponse(responseCode = "200", description = "Lista de recursos retornada")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar recurso", description = "Atualiza os dados de um recurso existente. Apenas administradores.")
    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado — requer papel ADMIN")
    @ApiResponse(responseCode = "404", description = "Recurso não encontrado")
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @Parameter(description = "ID do recurso") @PathVariable UUID id,
            @Valid @RequestBody ResourceRequestDTO data) {
        return ResponseEntity.ok(resourceService.updateResource(id, data));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar recurso", description = "Realiza o soft-delete de um recurso (desativa). Apenas administradores.")
    @ApiResponse(responseCode = "204", description = "Recurso desativado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado — requer papel ADMIN")
    @ApiResponse(responseCode = "404", description = "Recurso não encontrado")
    public ResponseEntity<Void> deleteResource(
            @Parameter(description = "ID do recurso") @PathVariable UUID id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
