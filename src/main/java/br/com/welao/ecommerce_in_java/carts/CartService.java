package br.com.welao.ecommerce_in_java.carts;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.itemsCart.ItemsCartRepository;
import br.com.welao.ecommerce_in_java.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockRepository stockRepository;

    public ResponseEntity<?> create(CartDTO cartDTO) {

        // verify quantity items in stock
        try {
            validQuantityItemStockAndUpdateQuantity(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        var user = this.cartRepository.findByUserId(cartDTO.getUser().getId());
        var cartIsOpen = this.cartRepository.findByPurchaseStatus(false);

        // verify if exists a user with a cart open
        if (user == null || user.isEmpty() && cartIsOpen == null) {
            createANewCart(cartDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Carts created successfully");
        }

        // if exists a cart open, update items cart or add new item cart
        List<ItemsCart> existingItems = cartIsOpen.getItemsCart();
        ItemsCart newItem = CartMapper.toEntity(cartDTO).getItemsCart().get(0);

        addNewItemsInCartOrUpdateExistsItems(existingItems, newItem, cartIsOpen);

        return ResponseEntity.status(HttpStatus.OK).body("Carts updated successfully");
    }

    private void validQuantityItemStockAndUpdateQuantity(CartDTO cartDTO) {
        var productID = cartDTO.getItemsCarts().get(0).getProducts().getId();
        var quantityItem = cartDTO.getItemsCarts().get(0).getQuantity();

        var price = cartDTO.getItemsCarts().get(0).getPrice();
        if (price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price for product ID: " + productID);
        }

        var quantityItemStock = this.stockRepository.findByProductsId(productID);
        if (quantityItemStock == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with ID %d not found", productID));
        }

        if (quantityItem > quantityItemStock.getQuantity() || quantityItem <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Product with ID %d has insufficient stock.", productID));
        }

        var debitStock = this.stockRepository.findByProductsId(productID);
        debitStock.setQuantity(debitStock.getQuantity() - quantityItem);

        this.stockRepository.save(debitStock);
    }

    private void createANewCart(CartDTO cartDTO) {
        double totalValue = cartDTO.getItemsCarts().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        cartDTO.setTotalValue((float)totalValue);
        cartDTO.setPurchaseStatus(false);

        Cart cart = CartMapper.toEntity(cartDTO);
        cart.getItemsCart().get(0).setCart(cart);

        this.cartRepository.save(cart);
    }

    private void addNewItemsInCartOrUpdateExistsItems(List<ItemsCart> existingItems, ItemsCart newItem, Cart cartIsOpen) {
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
    }

    public void updateTotalValueCart(ItemsCart hasItem) {
        Optional<Cart> cartOptional = this.cartRepository.findById(hasItem.getCart().getId());
        if (cartOptional.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }
        Cart cart = cartOptional.get();

        double totalValue = cart.getItemsCart().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        cart.setTotalValue((float) totalValue);
        this.cartRepository.save(cart);
    }


}
