package com.kilo.microkit.resources;

import com.kilo.microkit.core.provider.FlipkartProvider;
import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import com.kilo.microkit.views.HomeView;
import com.kilo.microkit.views.ItemsView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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
    public HomeView home() {

        if (categories == null) {
            categories = FlipkartProvider.categories(client, categoryDAO);
        }

        // sort map by value
        // Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(categories));
        // categories = ImmutableSortedMap.copyOf(categories, valueComparator);

        if (products == null) {
            products = FlipkartProvider.products(client, productDAO, getCategoryURL("laptops"));
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

    @GET
    @Path("/categories")
    @UnitOfWork
    public List<Category> categories() {

        categories = FlipkartProvider.categories(client, categoryDAO);
        return ( categories == null ) ? new ArrayList<Category>() :  categories ;
    }

    @GET
    @Path("/products/{category}")
    @UnitOfWork
    public List<Product> products(@PathParam("category") String category) {

        String categoryURL = getCategoryURL( category);
        List<Product> l = FlipkartProvider.products(client, productDAO, categoryURL);
        return l.subList(0, 15);

    }

    @POST
    @Path("/s")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public ItemsView searchP(@FormParam("searchTerm") String searchTerm) {

        List<Product> ret = null;
        try {
            ret = FlipkartProvider.search(client, searchTerm, 10);
            //search results must be indexed and stored
            productDAO.saveMany(ret);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView(ret);
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
