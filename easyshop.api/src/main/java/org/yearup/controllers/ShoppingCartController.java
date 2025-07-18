package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.sql.SQLException;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
public class ShoppingCartController {
    // a shopping cart requires
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ERROR!");
        }
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart item = shoppingCartDao.getCart(userId);
            if (item == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart NOT FOUND! " + userName);
            }

            // use the shoppingcartDao to get all items in the cart and return the cart
            return item;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addProduct(@PathVariable int productId, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ERROR!");
        }
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            if (productDao.getById(productId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID NOT FOUND!: " + productId);
            }
            shoppingCartDao.addProduct(userId, productId);
            return shoppingCartDao.getCart(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @PutMapping("/products/{productId}")
    public void updateProduct(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ERROR!");
        }
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        shoppingCartDao.updateProduct(userId, productId, item.getQuantity());
    }
    @DeleteMapping
    public void deleteProduct(Principal principal) throws SQLException {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ERROR!");
        }
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        shoppingCartDao.deleteProduct(userId);
    }

    // addProduct a POST method to addProduct a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added


    // addProduct a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // addProduct a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
