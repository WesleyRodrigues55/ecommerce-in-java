package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemsCartService {

    @Autowired
    private ItemsCartRepository itemsCartRepository;
}
