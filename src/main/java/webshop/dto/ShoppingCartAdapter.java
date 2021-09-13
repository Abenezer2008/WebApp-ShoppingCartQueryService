package webshop.dto;

import webshop.domain.ShoppingCart;

public class ShoppingCartAdapter {
    public static ShoppingCartDTO toDTO(ShoppingCart shoppingCart){
        return new ShoppingCartDTO(shoppingCart.getCardId(),shoppingCart.getCartItems());
    }
}
