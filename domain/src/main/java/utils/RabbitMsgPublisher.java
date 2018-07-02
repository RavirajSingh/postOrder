package utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMsgPublisher {
    private RabbitTemplate rabbitTemplate;

    public RabbitMsgPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void pushMsgToQueue(String exchange, String routingKey, Object message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
