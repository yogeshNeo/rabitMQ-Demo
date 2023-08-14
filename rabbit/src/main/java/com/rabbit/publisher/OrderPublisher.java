package com.rabbit.publisher;

import com.rabbit.config.MessagingConfig;
import com.rabbit.dto.Order;
import com.rabbit.dto.OrderStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderPublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/{restaurantName}")
    public void bookOrder(@RequestBody Order order, @PathVariable String restaurantName) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
           // Thread.sleep(1000);
            order.setOrderId(UUID.randomUUID().toString());
            OrderStatus orderStatus = new OrderStatus(order, "PROCESS", "order placed successfully in " + restaurantName);
            template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);
        }
        // return Mono.just("Success !!");
    }
}
