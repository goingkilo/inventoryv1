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
import java.util.List;

/**
 * Created by kraghunathan on 9/16/16.
 * TODO: move all if-null-then-fetch logic to provider (rename to manager ?)
 * TODO: move all db calls to provider to delegate to db
 * TODO: provider to manage categories, products lifecycles
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

    @POST
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView searchP(@DefaultValue("laptops") @FormParam("searchTerm") String searchTerm) {

        List<Product> searchResults = null;
        try {
            searchResults = FlipkartProvider.search(client, searchTerm, 30);
            //TODO: search results must be stored in ?
            //
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        //TODO: doing this too many times
        if( categories == null ) {
            categories = FlipkartProvider.categories(client, categoryDAO);
        }
        return new HomeView(categories, searchResults);
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
