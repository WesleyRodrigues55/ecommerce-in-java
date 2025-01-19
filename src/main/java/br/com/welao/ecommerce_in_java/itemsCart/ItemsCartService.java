package br.com.welao.ecommerce_in_java.itemsCart;

import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.carts.CartRepository;
import br.com.welao.ecommerce_in_java.carts.CartService;
import br.com.welao.ecommerce_in_java.stock.Stock;
import br.com.welao.ecommerce_in_java.stock.StockRepository;
import br.com.welao.ecommerce_in_java.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemsCartService {

    @Autowired
    private ItemsCartRepository itemsCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private StockService stockService;

    public ResponseEntity<?> remove(long itemsCartId) {
        Optional<ItemsCart> hasItemOptional = this.itemsCartRepository.findById(itemsCartId);
        if (hasItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ItemCarts not found");
        }

        ItemsCart hasItem = hasItemOptional.get();

        String message = validQuantityItemsCart(hasItem);

        long productId = hasItem.getProducts().getId();
        try {
            stockService.addsOneMoreToTheStock(productId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        try {
            cartService.updateTotalValueCart(hasItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    private String validQuantityItemsCart(ItemsCart hasItem) {
        if (hasItem.getQuantity() == 1) {
            this.itemsCartRepository.delete(hasItem);

            return "ItemCart successfully deleted";
        }

        hasItem.setQuantity(hasItem.getQuantity() - 1);
        this.itemsCartRepository.save(hasItem);

        return"Quantity ItemsCart removed successfully";
    }

    public ResponseEntity<?> listByUserId(long userId) {
       Optional<Cart> existingCart = this.cartRepository.findByUserIdAndPurchaseStatus(userId, false);
       if (existingCart.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
       }

       Cart cart = existingCart.get();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("cart", cart);

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
