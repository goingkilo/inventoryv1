package com.kilo.microkit.core.provider;


import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;

import javax.ws.rs.client.Client;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * FlipkartProvider provides following functionality
 * . a list of categories
 * . a list of products for a given category
 * . api to timecheck if both of above is not stale (ie less than 10 hrs old)
 * . search results for a given search term
 * TODO: offers and deals-of-the-day
 * TODO: strip out http calls (HTTPUtil) ,strip out db calls(FlipkartDBProvider, add FlipkartCache
 * TODO: where to put stale-checking ?
 * TODO :pagination decorator, sorter
 * TODO: search - caching, etc
 * <p>
 * Created by kraghunathan on
 * 9/8/16.
 */
public class FlipkartProvider {

    FlipkartDBProvider db;
    FlipkartHTTPProvider http;
    FlipkartCache cache;

    public FlipkartProvider() {

    }

    public FlipkartProvider(Client client, ProductDAO productDAO, CategoryDAO categoryDAO) {
        db = new FlipkartDBProvider(productDAO,categoryDAO);
        http = new FlipkartHTTPProvider(client);
    }

    public FlipkartProvider(FlipkartDBProvider db, FlipkartHTTPProvider http, FlipkartCache cache) {
        this.db = db;
        this.http = http;
        this.cache = cache;
    }

    /**
     * Search -
     *
     * @param searchTerm
     * @param count
     * @return
     */
    public List<Product> search( String searchTerm, int count) throws SocketTimeoutException {

        List<Product> remoteSearchResults = http.search( searchTerm, 30);
        List<Product> localSearchResults = db.search( searchTerm);

        remoteSearchResults.addAll(localSearchResults);

        return remoteSearchResults;
    }

    /**
     * Get list of categories
     * the order of retrieval is cache, db then http
     * TODO: This information expires in 10 hrs. lifecycle management needed
     *
     * @return
     */
    public List<Category> categories() {

        if( db.isStale()){

        }
        List<Category> categories = db.categories();
        if( categories == null || categories.size() == 0) {
            categories = http.categories();
            db.getCategoryDAO().saveMany(categories);
        }
        categories = db.categories();

        if (categories != null && categories.size() != 0) {
            Collections.sort(categories, new Comparator<Category>() {
                @Override
                public int compare(Category c1, Category c2) {
                    return c1.getTitle().compareTo(c2.getTitle());
                }
            });
            return categories;
        }

        // sort map by value
        // Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(categories));
        // categories = ImmutableSortedMap.copyOf(categories, valueComparator);

        return new ArrayList<Category>();
    }

    /**
     * Get list of products for a given category title
     * by either from DB if already existing or
     * fetching from affiliate API using corresponding fetch URL
     * TODO: this info expires in 10 hrs,needs isFresh()  & refresh()
     *
     * @param categoryURL
     * @return
     */
    public List<Product> products( String category, String categoryURL, String sort, int offset, int size) {

        List<Product>    products = db.products(category);

        if (products == null || products.size() == 0) {
            List<Product> products1 = http.products(category, categoryURL);
            db.getProductDAO().saveMany(products1 ,category);
        }
        products = db.products(category);

        switch( sort){
            case "brand":
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p1.getBrand().compareTo(p2.getBrand());
                    }
                });
                break;
            case "pricel":
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Float.valueOf(p1.getPrice()).compareTo(Float.valueOf(p2.getPrice()));
                    }
                });
                break;
            case "priceh":
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Float.valueOf(p2.getPrice()).compareTo(Float.valueOf(p1.getPrice()));
                    }
                });
                break;
        }


        if( products.size() > ( offset + size) ) {
            return products.subList(offset, offset + size);
        }

        return products;
    }

    /**
     * what we actually call
     *
     * @param categoryURL
     * @return
     */
    public List<Product> products( String category, String categoryURL, String sort) {
        return products( category, categoryURL, sort, 0, 30);
    }

}
