package com.shopping.shoppingcartmodule.Controller;


import com.shopping.shoppingcartmodule.DTO.ShoppingCartDTO;
import com.shopping.shoppingcartmodule.Service.Impl.ShoppingCartService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/shoppingcart")
public class ShoppingCartController {
    //@Autowired
    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping(value = "/create-cart/{userId}")
    public ShoppingCartDTO createCart(@PathVariable String userId) {

        return shoppingCartService.createCart(userId);
    }

    @PostMapping(value = "/add-cart/{cartid}/{productid}/{quantity}")
    public ShoppingCartDTO addProductToCart(@PathVariable long cartid, @PathVariable long productid, @PathVariable int quantity) {

        return shoppingCartService.addToCart(cartid, productid, quantity);
    }


    @DeleteMapping(value = "/removeproduct/{cartid}/{productid}")
    public ShoppingCartDTO deleteProduct(@PathVariable long cartid, @PathVariable long productid) {
        return shoppingCartService.deleteProductFromCart(cartid, productid);
    }

    @GetMapping(value = "/cart/{id}")
    public ShoppingCartDTO Cart(@PathVariable long id) {
        return shoppingCartService.getCart(id);
    }


    @DeleteMapping(value = "/removeallproduct/{cartid}")
    public ShoppingCartDTO deleteAllProduct(@PathVariable long cartid) {
        return shoppingCartService.deleteAllProduct(cartid);
    }

    @GetMapping(value = "/checkout/{cartId}")
    public String checkout(@PathVariable long cartId) {
        return shoppingCartService.checkout(cartId);
    }
}

