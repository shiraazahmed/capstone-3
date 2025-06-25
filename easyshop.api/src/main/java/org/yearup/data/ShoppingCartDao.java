package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // addProduct additional method signatures here
    ShoppingCart getCart(int userId);
    ShoppingCart addProduct(int userId, int productId);
    ShoppingCart updateProduct(int userId, int productId, int quantity);

}
