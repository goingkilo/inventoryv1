package com.kilo.microkit.core.provider;


import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.MetaInfoDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import java.util.List;

public class DBHelper {

    ProductDAO productDAO;



    MetaInfoDAO metaInfoDAO;
    CategoryDAO categoryDAO;

    String lastLoadTime;

    public DBHelper() {
    }

    public DBHelper(ProductDAO productDAO, CategoryDAO categoryDAO, MetaInfoDAO metaInfoDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.metaInfoDAO = metaInfoDAO;
    }

    public List<Product> search(String searchTerm) {
        List<Product> localSearchResults = productDAO.search( searchTerm);
        return localSearchResults;
    }

    public boolean isStale() {
        if( lastLoadTime == null || lastLoadTime.length() == 0 || lastLoadTime.equalsIgnoreCase("")) {
            lastLoadTime = metaInfoDAO.getLoadTime();
        }
        return (lastLoadTime == null) ? true :(Long.valueOf(lastLoadTime) + 10*60*60*1000) < System.currentTimeMillis();
    }


    public List<Category> categories() {

        return categoryDAO.getCategories();
    }

    public List<Product> products(  Category category) {
        return productDAO.getProducts(category.getTitle());
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public MetaInfoDAO getMetaInfoDAO() {
        return metaInfoDAO;
    }

}
