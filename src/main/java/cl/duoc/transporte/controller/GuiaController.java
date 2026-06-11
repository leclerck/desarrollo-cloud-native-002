package cl.duoc.transporte.controller;

import cl.duoc.transporte.dto.GuiaRequestDTO;
import cl.duoc.transporte.dto.GuiaResponseDTO;
import cl.duoc.transporte.service.GuiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
@RequiredArgsConstructor
public class GuiaController {

    private final GuiaService guiaService;

    @PostMapping
    public ResponseEntity<GuiaResponseDTO> create(@Valid @RequestBody GuiaRequestDTO dto) {
        return ResponseEntity.status(201).body(guiaService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guiaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuiaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody GuiaRequestDTO dto) {
        return ResponseEntity.ok(guiaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<GuiaResponseDTO> uploadToS3(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(guiaService.uploadToS3(id, file));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id, @RequestParam String requester) {
        byte[] content = guiaService.download(id, requester);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"guia-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GuiaResponseDTO>> search(
            @RequestParam String transportista,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(guiaService.findByTransportistaAndFecha(transportista, fecha));
    }
}
