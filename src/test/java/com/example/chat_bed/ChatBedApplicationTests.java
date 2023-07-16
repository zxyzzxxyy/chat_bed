package com.example.chat_bed;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatBedApplicationTests {

    @Autowired
    private RabbitTemplate rabbit;

    @Test
    void contextLoads() {
    }

    @Test
    void rabbitmq(){
        String queueName="simple.queue";
        String message="hello world";
        rabbit.convertAndSend(queueName,message);
    }
}
