package com.kilo.microkit.resources;

import com.kilo.microkit.core.provider.FlipkartProvider;
import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import com.kilo.microkit.views.HomeView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kraghunathan on 9/16/16.
 */
@Path("/a")
@Produces(MediaType.APPLICATION_JSON)
public class RetailResource {

    private final Client client;
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    String currentCategory = "laptops";

    // this is local cache for the home page
    List<Category> categories = null;
    List<Product> products = null;

    public RetailResource(ProductDAO productDAO, CategoryDAO categoryDAO, Client client) {

        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;

        this.client = client;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView home(@DefaultValue("laptops") @QueryParam("category") String category) {

        if (categories == null || categories.size() == 0 ) {
            categories = FlipkartProvider.categories(client, categoryDAO);
        }


    //TODO: product management. can't be doing a getAll from db.need getProduct(category)
        if (products == null || !category.equals(currentCategory)) {
            currentCategory = category;
            products = FlipkartProvider.products(client, productDAO, category, getCategoryURL(category));
        }

        return new HomeView(categories, products);
    }

    @GET
    @Path("/s/{searchTerm}")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView search(@PathParam("searchTerm") String searchTerm) {

        List<Product> products = null;
        try {
            products = FlipkartProvider.search(client, searchTerm, 10);

            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return (int) (Float.parseFloat(o1.getPrice()) - Float.parseFloat(o2.getPrice()));
                }
            });
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new HomeView(categories, products);
    }


    @POST
    @Path("/s")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView searchP(@DefaultValue("laptops") @FormParam("searchTerm") String searchTerm) {

        List<Product> ret = null;
        try {
            ret = FlipkartProvider.search(client, searchTerm, 10);
            //TODO: search results must be stored in ?
            //
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        if( categories == null ) {
            categories = FlipkartProvider.categories(client, categoryDAO);
        }
        return new HomeView(categories, ret);
    }

    private String getCategoryURL(String category){
        for ( Category c : categories) {
            if( c.getTitle().equalsIgnoreCase(category)) {
                return c.getUrl();
            }
        }
        return null;
    }



}
