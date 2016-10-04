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

    FlipkartProvider provider;

    public RetailResource(ProductDAO productDAO, CategoryDAO categoryDAO, Client client) {

        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;

        this.client = client;

        provider = new FlipkartProvider(client, productDAO, categoryDAO);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView home(@DefaultValue("laptops") @QueryParam("category") String category,
                         @DefaultValue("pricel") @QueryParam("sort") String sort) {

        List<Category> categories = provider.categories();
        currentCategory = category;
        List<Product> products = provider.products(category, getCategoryURL(categories, category), sort);

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

    private String getCategoryURL(List<Category> categories, String category){
        for ( Category c : categories) {
            if( c.getTitle().equalsIgnoreCase(category)) {
                return c.getUrl();
            }
        }
        return null;
    }



}
