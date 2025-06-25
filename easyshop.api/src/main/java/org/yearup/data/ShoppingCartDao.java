package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartDao {
    // addProduct additional method signatures here
    ShoppingCart getCart(int userId);
    void addProduct(int userId, int productId);
    ShoppingCart updateProduct(int userId, int productId, int quantity);
    ShoppingCart deleteProduct(int userId) throws SQLException;

}
