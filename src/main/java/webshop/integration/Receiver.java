package webshop.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import webshop.dto.EventDTO;
import webshop.service.CartQueryService;

@Service
public class Receiver {
    @Autowired
    private CartQueryService cartQueryService;

    @KafkaListener(topics = {"cartUpdated"})
    public void receiveUpdateEvent(@Payload EventDTO eventDTO, @Headers MessageHeaders headers){
        System.out.println("RECEIVED MESSAGE : " + eventDTO);
        cartQueryService.handle(eventDTO);
    }

    @KafkaListener(topics = {"cartCreated"})
    public void receiveCreateEvent(@Payload String cartId){
        System.out.println("CART WITH ID : " + cartId + " CREATED.");
        cartQueryService.add(cartId);
    }

    @KafkaListener(topics = {"cartDeleted"})
    public void receiveDeleteEvent(@Payload String cartId){
        System.out.println("CART WITH ID : " + cartId + " DELETED." );
        cartQueryService.deleteCart(cartId);
    }

}
