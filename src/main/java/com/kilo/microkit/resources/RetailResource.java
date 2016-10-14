package com.kilo.microkit.resources;

import com.kilo.microkit.core.provider.FlipkartProvider;
import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.MetaInfoDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import com.kilo.microkit.views.HomeView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by kraghunathan on 9/16/16.
 * TODO: move all if-null-then-fetch logic to provider (rename to manager ?)
 * TODO: move all db calls to provider to delegate to db
 * TODO: provider to manage categories, products lifecycles
 */
@Path("/a")
@Produces(MediaType.APPLICATION_JSON)
public class RetailResource {



    List<Category> categories = new ArrayList<>();
    Map<String,Category> categoryProductURLMap = new HashMap<String,Category>();

    String currentCategory = "mobiles";

    FlipkartProvider provider;

    public RetailResource(ProductDAO productDAO, CategoryDAO categoryDAO, MetaInfoDAO metaInfoDAO, Client client) {

        provider = new FlipkartProvider(client, productDAO, categoryDAO, metaInfoDAO);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView home(@DefaultValue("mobiles") @QueryParam("category") String categoryString,
                         @DefaultValue("pricel") @QueryParam("sort") String sort,
                         @DefaultValue("0") @QueryParam("offset") int offset,
                         @DefaultValue("30") @QueryParam("limit") int limit) {

        // this section goes to cacher
        if ( categories.size() == 0 ) {

            categories = provider.categories();

            categoryProductURLMap = new HashMap<>();

            for (Category c : categories) categoryProductURLMap.put(c.getTitle(), c);

            Collections.sort(categories, new Comparator<Category>() {
                @Override
                public int compare(Category c1, Category c2) {
                    return c1.getTitle().compareTo(c2.getTitle());
                }
                // sort map by value
                // Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(categories));
                // categories = ImmutableSortedMap.copyOf(categories, valueComparator);

            });
        }
        currentCategory = categoryString;

        List<Product> products = provider.products(categoryProductURLMap.get(currentCategory));

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


        if( products.size() > ( offset + limit) ) {
            products =  products.subList(offset, offset + limit);
        }

        return new HomeView(categories, products);
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView searchP(@DefaultValue("laptops") @FormParam("searchTerm") String searchTerm) {

        List<Product> searchResults = null;
        try {
            searchResults = provider.search(searchTerm, 30);
        } catch (SocketTimeoutException e1) {
            e1.printStackTrace();
        }

        List<Category> categories = provider.categories();
        return new HomeView(categories, searchResults);
    }

    @GET
    @UnitOfWork
    @Path("/load")
    //TODO:
    public String load() {
        return "OK";
    }

    private String getCategoryURL(List<Category> categories, String category){
        for ( Category c : categories) {
            if( c.getTitle().equalsIgnoreCase(category)) {
                return c.getUrl();
            }
        }
        return null;
    }



}
