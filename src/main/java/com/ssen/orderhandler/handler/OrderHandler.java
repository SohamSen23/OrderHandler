package com.ssen.orderhandler.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssen.orderhandler.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Random;

@Service
@Slf4j
public class OrderHandler {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private String destinationTopic = "order-delivered";
    private Long baseTimeTaken=1000L;

    public void handlerOrders(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException, InterruptedException {
        Order order = mapper.readValue(consumerRecord.value(), Order.class);
        order.setOrderedAt(System.currentTimeMillis());
        prepareOrder();
        order.setTimeTaken(System.currentTimeMillis() - order.getOrderedAt());

        String key = String.valueOf(order.getId());
        String value = mapper.writeValueAsString(order);

        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(new ProducerRecord<>(destinationTopic,  key, value));

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Exception sending Kafka message", ex.getMessage());
                try {
                    throw ex;
                } catch (Throwable throwable) {
                    log.error("Error in OnFailure: {}", throwable.getMessage());
                }
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message sent; Key : {} ; Value {} , partition is {}", key, value,
                        result.getRecordMetadata().partition());
            }
        });

    }

    private void prepareOrder() throws InterruptedException {
        Random random = new Random();
        int timeMultiplier = random.nextInt(10) + 1;
        Thread.sleep(timeMultiplier*baseTimeTaken); // Mimics an order preparation; cannot predict how long it will take
    }

}
