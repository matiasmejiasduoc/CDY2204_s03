package cl.duoc.gestion_guias.service;

import cl.duoc.gestion_guias.dto.GuideResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuideProducer {

    private static final Logger log = LoggerFactory.getLogger(GuideProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendGuide(GuideResponseDTO guide) {
        try {
            rabbitTemplate.convertAndSend("guias-exchange", "cola-guias", guide);
        } catch (Exception ex) {
            log.error("Error al publicar la guía en RabbitMQ", ex);
        }
    }
}
