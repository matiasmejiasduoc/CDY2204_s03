package cl.duoc.gestion_guias.repository;

import cl.duoc.gestion_guias.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    List<Guide> findByCarrierAndDate(String carrier, LocalDate date);

    List<Guide> findByCarrier(String carrier);
}
