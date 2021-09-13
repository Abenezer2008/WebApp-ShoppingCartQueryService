package webshop.service;

import webshop.domain.CartItem;
import webshop.dto.EventDTO;
import webshop.dto.ProductDTO;
import webshop.dto.ShoppingCartDTO;

import java.util.List;

public interface CartQueryService {
    void add(String cartId);
    void addToCart(String cartId, ProductDTO productDTO, int quantity);
    void removeFromCart(String cartId, ProductDTO productDTO, int quantity);
    void deleteCart(String cartId);
    List<CartItem> getCartItems(String cartId);
    ShoppingCartDTO getCart(String cartId);
    void handle(EventDTO eventDTO);
    Integer getQuantityInCart(String cartId, String productId);
}
