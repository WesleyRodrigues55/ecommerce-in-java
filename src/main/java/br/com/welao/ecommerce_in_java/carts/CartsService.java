package br.com.welao.ecommerce_in_java.carts;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.itemsCart.ItemsCartRepository;
import br.com.welao.ecommerce_in_java.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartsService {

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private ItemsCartRepository itemsCartRepository;

    private float sumTotalValeuCart(List<ItemsCart> itemsCart) {
        return (float) itemsCart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private void createANewCart(CartsDTO cartsDTO) {
        float totalValue = sumTotalValeuCart(cartsDTO.getItemsCarts());

        cartsDTO.setTotalValue(totalValue);
        cartsDTO.setPurchaseStatus(false);

        Carts carts = CartsMapper.toEntity(cartsDTO);
        if (carts.getItemsCarts() != null) {
            carts.getItemsCarts().forEach(item -> item.setCarts(carts));
        }

        this.cartsRepository.save(carts);
    }

    private void addNewItemsInCartOrUpdateExistsItems(List<ItemsCart> existingItems, List<ItemsCart> newItems, Carts cartIsOpen) {
        if (newItems != null) {
            newItems.forEach(newItem -> {
                Optional<ItemsCart> existingItem = existingItems.stream()
                        .filter(item -> item.getProducts().getId() == newItem.getProducts().getId())
                        .findFirst();

                if (existingItem.isPresent()) {
                    ItemsCart itemToUpdate = existingItem.get();
                    itemToUpdate.setQuantity(itemToUpdate.getQuantity() + newItem.getQuantity());
                    itemToUpdate.setPrice(newItem.getPrice());
                } else {
                    newItem.setCarts(cartIsOpen);
                    existingItems.add(newItem);
                }
            });
        }

        float updatedTotalValue = sumTotalValeuCart(existingItems);
        cartIsOpen.setTotalValue(updatedTotalValue);

        this.cartsRepository.save(cartIsOpen);
    }

    public ResponseEntity<?> create(CartsDTO cartsDTO) {
        var user = this.cartsRepository.findByUserId(cartsDTO.getUser().getId());
        var cartIsOpen = this.cartsRepository.findByPurchaseStatus(false);

        if (user == null || user.isEmpty() && cartIsOpen == null) {

            /*
                VALIDATE STOCK BEFORE INSERT
             */
            createANewCart(cartsDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Carts created successfully");
        }

        List<ItemsCart> existingItems = cartIsOpen.getItemsCarts();
        List<ItemsCart> newItems = CartsMapper.toEntity(cartsDTO).getItemsCarts();

        /*
            VALIDATE STOCK BEFORE INSERT
         */
        addNewItemsInCartOrUpdateExistsItems(existingItems, newItems, cartIsOpen);

        return ResponseEntity.status(HttpStatus.OK).body("Carts updated successfully");
    }


    public ResponseEntity<?> list() {
        var carts = cartsRepository.findAll();


        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }
}
