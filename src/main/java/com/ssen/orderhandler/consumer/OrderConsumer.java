package com.ssen.orderhandler.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssen.orderhandler.handler.OrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @Autowired
    OrderHandler orderHandler;

    @KafkaListener(topics = {"orders"})
    public void onMessage(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException, InterruptedException {
        log.info("ConsumerRecord : {} ", consumerRecord );
        orderHandler.handlerOrders(consumerRecord);
    }
}
