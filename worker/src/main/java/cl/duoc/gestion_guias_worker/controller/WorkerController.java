package cl.duoc.gestion_guias_worker.controller;

import cl.duoc.gestion_guias_worker.model.ProcessedGuide;
import cl.duoc.gestion_guias_worker.repository.ProcessedGuideRepository;
import cl.duoc.gestion_guias_worker.service.GuideConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class WorkerController {

    private final GuideConsumer guideConsumer;
    private final ProcessedGuideRepository processedGuideRepository;

    @PostMapping("/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Integer>> processPendingMessages() {
        int processed = guideConsumer.processPendingMessages();
        return ResponseEntity.ok(Map.of("processed", processed));
    }

    @GetMapping("/processed")
    @PreAuthorize("hasAnyRole('ADMIN','DESCARGA')")
    public ResponseEntity<List<ProcessedGuide>> findProcessed() {
        return ResponseEntity.ok(processedGuideRepository.findAll());
    }
}
