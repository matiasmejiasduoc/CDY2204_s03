package cl.duoc.gestion_guias.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "cola-guias";
    public static final String DLQ = "cola-guias-error";
    public static final String EXCHANGE = "guias-exchange";
    public static final String DLX = "guias-dlx";
    public static final String ROUTING_KEY = "guia";
    public static final String DLQ_ROUTING_KEY = "guia.error";

    @Bean
    public Queue guiaQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue guiaErrorQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public DirectExchange guiasExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange guiasDlx() {
        return new DirectExchange(DLX);
    }

    @Bean
    public Binding guiaBinding(Queue guiaQueue, DirectExchange guiasExchange) {
        return BindingBuilder.bind(guiaQueue)
                .to(guiasExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding guiaErrorBinding(Queue guiaErrorQueue, DirectExchange guiasDlx) {
        return BindingBuilder.bind(guiaErrorQueue)
                .to(guiasDlx)
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
