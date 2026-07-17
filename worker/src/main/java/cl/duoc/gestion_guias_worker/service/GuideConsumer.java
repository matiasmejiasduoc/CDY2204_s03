package cl.duoc.gestion_guias_worker.service;

import cl.duoc.gestion_guias_worker.config.RabbitMQConfig;
import cl.duoc.gestion_guias_worker.dto.GuideMessageDTO;
import cl.duoc.gestion_guias_worker.model.ProcessedGuide;
import cl.duoc.gestion_guias_worker.repository.ProcessedGuideRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GuideConsumer {

    private static final Logger log = LoggerFactory.getLogger(GuideConsumer.class);

    private final ProcessedGuideRepository processedGuideRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public int processPendingMessages() {
        Integer processed = rabbitTemplate.execute(channel -> {
            int count = 0;
            GetResponse response;
            while ((response = channel.basicGet(RabbitMQConfig.QUEUE, false)) != null) {
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                try {
                    GuideMessageDTO guide = objectMapper.readValue(response.getBody(), GuideMessageDTO.class);
                    processGuide(guide);
                    channel.basicAck(deliveryTag, false);
                    count++;
                } catch (Exception ex) {
                    log.error("Error al procesar mensaje de {}, RabbitMQ lo enviará a la DLQ", RabbitMQConfig.QUEUE, ex);
                    channel.basicNack(deliveryTag, false, false);
                }
            }
            return count;
        });
        return processed != null ? processed : 0;
    }

    private void processGuide(GuideMessageDTO guide) {
        // Fallo simulado: permite demostrar en la presentación el paso automático a la DLQ
        if (guide.getGuideNumber() != null && guide.getGuideNumber().startsWith("ERR")) {
            throw new IllegalStateException("Fallo simulado para demostrar el flujo hacia la DLQ");
        }

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
    }
}
