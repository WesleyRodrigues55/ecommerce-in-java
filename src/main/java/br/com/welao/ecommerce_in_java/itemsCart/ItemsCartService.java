package br.com.welao.ecommerce_in_java.itemsCart;

import br.com.welao.ecommerce_in_java.carts.CartRepository;
import br.com.welao.ecommerce_in_java.carts.CartService;
import br.com.welao.ecommerce_in_java.stock.StockRepository;
import br.com.welao.ecommerce_in_java.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemsCartService {

    @Autowired
    private ItemsCartRepository itemsCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockRepository stockRepository;

    public ResponseEntity<?> remove(long id) {
        Optional<ItemsCart> hasItemOptional = this.itemsCartRepository.findById(id);
        if (hasItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ItemCarts not found");
        }

        ItemsCart hasItem = hasItemOptional.get();

        try {
            if (hasItem.getQuantity() == 1) {
                this.itemsCartRepository.delete(hasItem);
            } else {
                hasItem.setQuantity(hasItem.getQuantity() - 1);
                this.itemsCartRepository.save(hasItem);
            }

            StockService stockService = new StockService();
            stockService.addsOneMoreToTheStock(hasItem);

            CartService cartService = new CartService();
            cartService.updateTotalValueCart(hasItem);

            String message = hasItem.getQuantity() == 0
                    ? "ItemCart successfully deleted"
                    : "Quantity ItemsCart removed successfully";

            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
