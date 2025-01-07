package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items-cart")
public class ItemsCartController {

    @Autowired
    private ItemsCartService itemsCartService;

}
