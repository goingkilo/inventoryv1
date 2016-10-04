package com.kilo.microkit.core.provider;


import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import java.util.List;

public class FlipkartDBProvider {

    ProductDAO productDAO;
    CategoryDAO categoryDAO;

    public FlipkartDBProvider() {
    }

    public FlipkartDBProvider(ProductDAO productDAO, CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    public List<Product> search(String searchTerm) {
        List<Product> localSearchResults = productDAO.search( searchTerm);
        return localSearchResults;
    }

    public boolean isStale() {
        return false;
    }


    public List<Category> categories() {

        return categoryDAO.getCategories();
    }

    public List<Product> products(  String category) {
        return productDAO.getProducts(category);
    }

    public boolean isFresh() {
        return true;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

}
