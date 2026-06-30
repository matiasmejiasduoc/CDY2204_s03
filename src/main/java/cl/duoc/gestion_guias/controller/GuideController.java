package cl.duoc.gestion_guias.controller;

import cl.duoc.gestion_guias.dto.GuideRequestDTO;
import cl.duoc.gestion_guias.dto.GuideResponseDTO;
import cl.duoc.gestion_guias.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @PostMapping
    public ResponseEntity<GuideResponseDTO> create(@Valid @RequestBody GuideRequestDTO dto) {
        return ResponseEntity.status(201).body(guideService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> update(@PathVariable Long id, @Valid @RequestBody GuideRequestDTO dto) {
        return ResponseEntity.ok(guideService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guideService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<GuideResponseDTO> uploadToS3(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.uploadToS3(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id, @RequestParam String requester) {
        byte[] content = guideService.download(id, requester);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"guide-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GuideResponseDTO>> search(
            @RequestParam String carrier,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(guideService.findByCarrierAndDate(carrier, date));
    }
}
