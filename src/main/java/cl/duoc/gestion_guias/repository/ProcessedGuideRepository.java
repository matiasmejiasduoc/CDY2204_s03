package cl.duoc.gestion_guias.repository;

import cl.duoc.gestion_guias.model.ProcessedGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedGuideRepository extends JpaRepository<ProcessedGuide, Long> {
}
