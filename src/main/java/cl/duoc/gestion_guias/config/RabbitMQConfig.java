package cl.duoc.gestion_guias.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue guiaQueue() {
        return new Queue("cola-guias", true);
    }

    @Bean
    public Queue guiaErrorQueue() {
        return new Queue("cola-guias-error", true);
    }

    @Bean
    public DirectExchange guiasExchange() {
        return new DirectExchange("guias-exchange");
    }

    @Bean
    public Binding guiaBinding(Queue guiaQueue, DirectExchange guiasExchange) {
        return BindingBuilder.bind(guiaQueue)
                .to(guiasExchange)
                .with("guia");
    }

    @Bean
    public Binding guiaErrorBinding(Queue guiaErrorQueue, DirectExchange guiasExchange) {
        return BindingBuilder.bind(guiaErrorQueue)
                .to(guiasExchange)
                .with("guia.error");
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
