package webshop.dto;

import webshop.domain.CartItem;

import java.util.List;

public class CartItems {
    private List<CartItem> cartItems;
    public CartItems(){}
    public CartItems(List<CartItem> cartItems){
        this.cartItems = cartItems;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItems=" + cartItems +
                '}';
    }
}
