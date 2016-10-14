package com.kilo.microkit.core.provider;


import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.MetaInfoDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import javax.ws.rs.client.Client;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * categories, products, deals  & offers ,search
 * TODO: offers and deals-of-the-day
 * TODO :pagination decorator, sorter - this is done in the resource
 * TODO: search - caching, etc
 * <p>
 * Created by kraghunathan on
 * 9/8/16.
 */
public class FlipkartProvider {

    DBHelper db;
    HTTPClient http;

    public FlipkartProvider() {

    }

    public FlipkartProvider(Client client, ProductDAO productDAO, CategoryDAO categoryDAO, MetaInfoDAO metaInfoDAO) {
        db = new DBHelper(productDAO,categoryDAO,metaInfoDAO);
        http = new HTTPClient(client);
    }

    /**
     * Search -
     * @param searchTerm
     * @param count
     * @return
     */
    public List<Product> search( String searchTerm, int count) throws SocketTimeoutException {

        List<Product> remoteSearchResults = http.search( searchTerm, 30);
        List<Product> localSearchResults = db.search( searchTerm);

        if( localSearchResults != null && localSearchResults.size() > 0)
            remoteSearchResults.addAll(localSearchResults);

        return remoteSearchResults;
    }

    /**
     * Get list of categories
     */
    public List<Category> categories() {

        if( db.isStale()) {
            db.getCategoryDAO().saveMany(http.categories());
        }

        List<Category> categories = db.categories();

        return (categories == null ) ?  new ArrayList<Category>() : categories ;
    }

    /**
     * Get list of products for a given category
     */
    public List<Product> products( Category category) {

        if( db.isStale()) {
            db.getProductDAO().saveMany(http.products(category), category.getTitle());
        }

        List<Product>    products = db.products(category);

        return (products == null ) ?  new ArrayList<Product>(): products;
    }


}
