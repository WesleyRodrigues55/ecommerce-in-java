package br.com.welao.ecommerce_in_java.carts;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public ResponseEntity<?> create(CartDTO cartDTO) {
        var user = this.cartRepository.findByUserId(cartDTO.getUser().getId());
        var cartIsOpen = this.cartRepository.findByPurchaseStatus(false);

        if (user == null || user.isEmpty() && cartIsOpen == null) {
            double totalValue = cartDTO.getItemsCarts().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();

            cartDTO.setTotalValue((float)totalValue);
            cartDTO.setPurchaseStatus(false);

            Cart cart = CartMapper.toEntity(cartDTO);
            cart.getItemsCart().get(0).setCart(cart);

            this.cartRepository.save(cart);

            return ResponseEntity.status(HttpStatus.CREATED).body("Carts created successfully");
        }

        List<ItemsCart> existingItems = cartIsOpen.getItemsCart();
        ItemsCart newItem = CartMapper.toEntity(cartDTO).getItemsCart().get(0);

        if (newItem != null) {
            Optional<ItemsCart> existingItem = existingItems.stream()
                    .filter(item -> item.getProducts().getId() == newItem.getProducts().getId())
                    .findFirst();

            if (existingItem.isPresent()) {
                ItemsCart itemToUpdate = existingItem.get();
                itemToUpdate.setQuantity(itemToUpdate.getQuantity() + newItem.getQuantity());
                itemToUpdate.setPrice(newItem.getPrice());
            } else {
                newItem.setCart(cartIsOpen);
                existingItems.add(newItem);
            }
        }

        double updatedTotalValue = existingItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();


        cartIsOpen.setTotalValue((float) updatedTotalValue);

        this.cartRepository.save(cartIsOpen);

        return ResponseEntity.status(HttpStatus.OK).body("Carts updated successfully");
    }


    public ResponseEntity<?> list() {
        var carts = cartRepository.findAll();


        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }
}
