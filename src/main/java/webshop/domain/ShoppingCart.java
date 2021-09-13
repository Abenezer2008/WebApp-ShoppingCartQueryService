package webshop.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Document
public class ShoppingCart {
    @Id
    private String cardId;
    private Map<String  ,CartItem> cartItems;

    public ShoppingCart(){}

    public ShoppingCart(String cardId) {
        this.cardId = cardId;
        cartItems = new HashMap<>();
    }

    public void addProductToCartItem(Product product,int quantity){
        if(cartItems.containsKey(product.getProductNumber())){
            CartItem previous = cartItems.get(product.getProductNumber());
            CartItem latest = new CartItem(this.cardId,product, previous.getQuantity() + quantity);
            cartItems.put(product.getProductNumber(),latest);
        }
        else{
            CartItem cartItem = new CartItem(this.cardId,product,quantity);
            cartItems.put(product.getProductNumber(),cartItem);
        }
    }

    public void removeProductFromCartItem(Product product,int quantity){
        if(cartItems.containsKey(product.getProductNumber())){
            CartItem previous = cartItems.get(product.getProductNumber());
            CartItem latest = new CartItem(this.cardId,product, previous.getQuantity() - quantity);
            cartItems.put(product.getProductNumber(),latest);
        }
    }

    public String getCardId() {
        return cardId;
    }

    public List<CartItem> getCartItems(){
        return this.cartItems.values().stream().collect(Collectors.toList());
    }


}
