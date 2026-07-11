package cl.duoc.gestion_guias.service;

import cl.duoc.gestion_guias.dto.GuideResponseDTO;
import cl.duoc.gestion_guias.model.ProcessedGuide;
import cl.duoc.gestion_guias.repository.ProcessedGuideRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GuideConsumer {

    private static final Logger log = LoggerFactory.getLogger(GuideConsumer.class);

    private final ProcessedGuideRepository processedGuideRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "cola-guias")
    @Transactional
    public void listen(GuideResponseDTO guide) {
        processGuide(guide);
    }

    @Transactional
    public void processPendingMessages() {
        Object message;
        while ((message = rabbitTemplate.receiveAndConvert("cola-guias")) != null) {
            if (message instanceof GuideResponseDTO guide) {
                processGuide(guide);
            } else {
                log.warn("Mensaje recibido en formato inesperado desde cola-guias: {}", message);
            }
        }
    }

    private void processGuide(GuideResponseDTO guide) {
        try {
            ProcessedGuide processedGuide = new ProcessedGuide();
            processedGuide.setGuideNumber(guide.getGuideNumber());
            processedGuide.setCarrier(guide.getCarrier());
            processedGuide.setDate(guide.getDate());
            processedGuide.setRecipient(guide.getRecipient());
            processedGuide.setDestinationAddress(guide.getDestinationAddress());
            processedGuide.setCargoDescription(guide.getCargoDescription());
            processedGuide.setStatus(guide.getStatus());
            processedGuide.setProcessedAt(LocalDateTime.now());

            processedGuideRepository.save(processedGuide);
        } catch (Exception ex) {
            log.error("Error al procesar la guía desde RabbitMQ", ex);
            rabbitTemplate.convertAndSend("guias-exchange", "cola-guias-error", guide);
        }
    }
}
