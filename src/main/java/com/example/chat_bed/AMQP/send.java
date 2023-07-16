package com.example.chat_bed.AMQP;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class send {
    @Autowired
    private RabbitTemplate rabbit;

    public void systemmsgsendsuccess(String msg){
        String queueName="login.exchange";
        rabbit.convertAndSend(queueName,"success",msg);
    }

    public void systemmsgsendwarn(String msg){
        String queueName="login.exchange";
        rabbit.convertAndSend(queueName,"warn",msg);
    }
}
