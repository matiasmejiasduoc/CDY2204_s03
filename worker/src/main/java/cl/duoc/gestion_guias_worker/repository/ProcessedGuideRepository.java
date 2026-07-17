package cl.duoc.gestion_guias_worker.repository;

import cl.duoc.gestion_guias_worker.model.ProcessedGuide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedGuideRepository extends JpaRepository<ProcessedGuide, Long> {
}
