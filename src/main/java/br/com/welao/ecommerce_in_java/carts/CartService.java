package br.com.welao.ecommerce_in_java.carts;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.itemsCart.ItemsCartRepository;
import br.com.welao.ecommerce_in_java.stock.Stock;
import br.com.welao.ecommerce_in_java.stock.StockRepository;
import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.utils.Utils;
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

        // move position
        try {
            validQuantityItemStockAndUpdateQuantity(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        var userId = cartDTO.getUser().getId();
        Optional<Cart> existingUserInCart = this.cartRepository.findByUserIdAndPurchaseStatus(userId, false);

        if (existingUserInCart.isEmpty()) {
            createANewCart(cartDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Carts created successfully");
        }

        Cart cart = existingUserInCart.get();

        List<ItemsCart> existingItems = cart.getItemsCart();
        ItemsCart newItem = CartMapper.toEntity(cartDTO).getItemsCart().get(0);

        addNewItemsInCartOrUpdateExistsItems(existingItems, newItem, cart);

        return ResponseEntity.status(HttpStatus.OK).body("Carts updated successfully");
    }

    private void validQuantityItemStockAndUpdateQuantity(CartDTO cartDTO) {
        var productID = cartDTO.getItemsCarts().get(0).getProducts().getId();
        var quantityItem = cartDTO.getItemsCarts().get(0).getQuantity();

        var price = cartDTO.getItemsCarts().get(0).getPrice();
        if (price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price for product ID: " + productID);
        }

        Optional<Stock> quantityItemStock = this.stockRepository.findByProductsId(productID);

        if (quantityItemStock.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with ID %d not found", productID));
        }

        Stock stock = quantityItemStock.get();

        if (quantityItem > stock.getQuantity() || quantityItem <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Product with ID %d has insufficient stock.", productID));
        }

        Optional<Stock> debitStock = this.stockRepository.findByProductsId(productID);
        if (debitStock.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with ID %d not found", productID));
        }

        Stock debit = debitStock.get();
        debit.setQuantity(debit.getQuantity() - quantityItem);

        this.stockRepository.save(debit);
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

    public void updatedStatusCart(long cartId, float amount) {
        Optional<Cart> existingCart = this.cartRepository.findById(cartId);
        if (existingCart.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }

        Cart cart = existingCart.get();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setPurchaseStatus(true);
        cartDTO.setTotalValue(amount);
        Utils.copyNonNullProperties(cartDTO, cart);
        this.cartRepository.save(cart);
    }

}
