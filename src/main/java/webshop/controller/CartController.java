package webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.dto.CartItems;
import webshop.dto.ShoppingCartDTO;
import webshop.service.CartQueryService;

@RestController
public class CartController {
    @Autowired
    private CartQueryService cartQueryService;

    @Autowired
    OrderFeignClient orderFeignClient;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/carts/query/cart/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable String cartId){
        ShoppingCartDTO cartDTO = cartQueryService.getCart(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/carts/query/cart/{cartId}/product/{productId}")
    public Integer getProductQuantityInCart(@PathVariable String cartId,@PathVariable String productId){
        Integer quantity = cartQueryService.getQuantityInCart(cartId,productId);
        return quantity;
    }


    @PostMapping("/carts/query/checkout/{cartId}/order/{orderId}")
    public ResponseEntity<?> checkout(@PathVariable String cartId,@PathVariable String orderId){
        //Circuit breaker for feign client
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

        ShoppingCartDTO cartDTO = cartQueryService.getCart(cartId);

        return circuitBreaker.run(() -> orderFeignClient.checkout(orderId,cartDTO),throwable -> getFallBackResponse());
    }

    @GetMapping("/carts/query/cart-items/{cartId}")
    public ResponseEntity<?> getCartItems(@PathVariable String cartId){
        CartItems cartItems = new CartItems(cartQueryService.getCartItems(cartId));
        return new ResponseEntity<>(cartItems,HttpStatus.OK);
    }

    @FeignClient(name = "OrderService")
    interface OrderFeignClient{
        @PostMapping("/orders/{orderId}")
        ResponseEntity<?> checkout(@PathVariable String orderId, @RequestBody ShoppingCartDTO shoppingCartDTO);
    }

    private ResponseEntity<?> getFallBackResponse(){
        return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }
}
