package webshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webshop.domain.CartItem;
import webshop.domain.Detail;
import webshop.domain.Product;
import webshop.domain.ShoppingCart;
import webshop.dto.*;
import webshop.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartQueryServiceImpl implements CartQueryService{
    private static final Logger logger = LoggerFactory.getLogger(CartQueryServiceImpl.class.getName());

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Override
    public void add(String cartId) {
        logger.info("Calling add");
        ShoppingCart cart = new ShoppingCart(cartId);
        cartRepository.save(cart);
        logger.info("Creating shopping cart");
    }

    @Override
    public void addToCart(String cartId, ProductDTO productDTO, int quantity) {
        logger.info("Calling add to cart");
        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(cartId);
        if(optionalShoppingCart.isPresent()){
            ShoppingCart cart = optionalShoppingCart.get();
            Product product = ProductAdapter.fromDTO(productDTO);
            cart.addProductToCartItem(product,quantity);
            logger.info("Adding item to cart");
            cartRepository.save(cart);
        } else logger.error("Add to cart with invalid id");
    }

    @Override
    public void removeFromCart(String cartId, ProductDTO productDTO, int quantity) {
        logger.info("Calling remove from cart");
        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(cartId);
        if(optionalShoppingCart.isPresent()){
            ShoppingCart cart = optionalShoppingCart.get();
            Product product = ProductAdapter.fromDTO(productDTO);
            cart.removeProductFromCartItem(product,quantity);
            cartRepository.save(cart);
            logger.info("Removing item from cart");
        } else logger.error("Remove from cart with invalid id");
    }

    @Override
    public void deleteCart(String cartId) {
        logger.info("Calling delete cart");
        cartRepository.deleteById(cartId);
    }

    @Override
    public List<CartItem> getCartItems(String cartId) {
        logger.info("Calling get cart items");
        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(cartId);
        if(optionalShoppingCart.isPresent()){
            logger.info("Getting cart items");
            return optionalShoppingCart.get().getCartItems();
        }
        logger.error("Get cart items with invalid cart id");
        return null;
    }

    @Override
    public ShoppingCartDTO getCart(String cartId) {
        logger.info("Calling get cart");
        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(cartId);
        if(optionalShoppingCart.isPresent()){
            logger.info("Getting cart");
            return ShoppingCartAdapter.toDTO(optionalShoppingCart.get());
        }
        logger.error("Get cart with invalid cart id");
        return null;
    }

    @Override
    public void handle(EventDTO eventDTO) {
        logger.info("Calling handle");
        Detail detail = eventDTO.getDetail();
        String cartId = eventDTO.getCartId();
        ProductDTO productDTO = eventDTO.getProductDTO();
        int quantity = eventDTO.getQuantity();
        logger.info("Handling event");
        if(detail.equals(Detail.PRODUCT_ADDED)){
            addToCart(cartId,productDTO,quantity);
        }
        else if(detail.equals(Detail.PRODUCT_REMOVED)){
            removeFromCart(cartId,productDTO,quantity);
        }
    }

    @Override
    public Integer getQuantityInCart(String cartId, String productId) {
        logger.info("Calling get Quantity In Cart");
        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(cartId);
        if(optionalShoppingCart.isPresent()){
            logger.info("Getting quantity of product in cart");
            return optionalShoppingCart.get().getCartItems().stream().filter(cartItem -> cartItem.getProduct().getProductNumber() == productId).mapToInt(CartItem::getQuantity).sum();
        }
        logger.error("Get cart with invalid cart id");
        return null;
    }
}
