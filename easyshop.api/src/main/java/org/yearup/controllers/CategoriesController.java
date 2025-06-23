package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    // create an Autowired controller to inject the categoryDao and ProductDao

    // add the appropriate annotation for a get action
    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @RequestMapping(path = "/{categoryId}/products", method = RequestMethod.GET)
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
        // insert the category
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id
        categoryDao.update(id, category);

    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
        categoryDao.delete(id);
    }
}
