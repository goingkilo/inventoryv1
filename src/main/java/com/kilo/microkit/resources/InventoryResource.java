package com.kilo.microkit.resources;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.kilo.microkit.api.parser.Parser;
import com.kilo.microkit.api.util.APIFeeds;
import com.kilo.microkit.api.util.AffiliateAPIException;
import com.kilo.microkit.api.model.ProductInfo;
import com.kilo.microkit.api.dao.InventoryDAO;
import com.kilo.microkit.api.model.InventoryItem;
import com.kilo.microkit.api.util.FlipKart;
import com.kilo.microkit.views.HomeView;
import com.kilo.microkit.views.ItemsView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by kraghunathan on 9/16/16.
 */
@Path("/a")
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private final Client client;
    private final InventoryDAO inventoryDAO;

    Map<String, String> categories = null;
    List<ProductInfo> products = null;

    public InventoryResource(InventoryDAO inventoryDAO, Client client) {
        this.inventoryDAO = inventoryDAO;
        this.client = client;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView home() {

        APIFeeds feeds = new APIFeeds("goingkilo", "1368e5baaf8e4bcdb442873d4aa8ef6e", "no");
        try {
            if (categories == null) {
                Map<String,String> cats = feeds.categories();

                categories = new HashMap<String,String>();

                for( String key : cats.keySet()){
                    // change 'hitchhiker_guide' to 'Hitchhiker Guide'
                    Function capitalizer = new Function<String,String>(){
                        @Nullable
                        @Override
                        public String apply(String s) {
                            return s.substring(0, 1).toUpperCase() + s.substring(1);
                        }
                    };

                    String capSpaced = Joiner.on(" ")
                            .join(
                                    Lists.transform(
                                            Splitter.onPattern("_").splitToList(key), capitalizer).toArray(new String[]{}));
                    categories.put(key, capSpaced);
                }
            }
            //sort categories
            Ordering<String> valueComparator = Ordering.natural().onResultOf(Functions.forMap(categories));
            categories = ImmutableSortedMap.copyOf(categories, valueComparator);

            if (products == null) {
                products = feeds.products("laptops");
            }
        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        }
        return new HomeView(categories, products);
    }

    @GET
    @Path("/s/{searchTerm}")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public ItemsView search(@PathParam("searchTerm") String searchTerm) {

        List<InventoryItem> ret = null;
        try {
            ret = FlipKart.search(client, searchTerm, 10);
            Collections.sort(ret, new Comparator<InventoryItem>() {
                @Override
                public int compare(InventoryItem o1, InventoryItem o2) {
                    return (int) (Float.parseFloat(o1.getPrice()) - Float.parseFloat(o2.getPrice()));
                }
            });
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView(ret);
    }

    @GET
    @Path("/categories")
    @UnitOfWork
    public String[] categories() {

        APIFeeds feeds = new APIFeeds("goingkilo", "1368e5baaf8e4bcdb442873d4aa8ef6e", "no");
        try {

            Map<String, String> categories = feeds.categories();
            String[] ret = categories.keySet().toArray(new String[]{});
            Arrays.sort(ret);
            return ret;

        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    @GET
    @Path("/products/{category}")
    @UnitOfWork
    public List<InventoryItem> products(@PathParam("category") String category) {

        Map<String, String> cats = FlipKart.categories();
        String first = cats.values().iterator().next();
        System.out.println(">>:" + first);
        try {
            String a = FlipKart.get(client, first, 10);
            System.out.println( ">>->>:\n"+a);
            List<InventoryItem> b = Parser.parseSearchResults(a);
            return b;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ArrayList<InventoryItem>();
    }

    @POST
    @Path("/s")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public ItemsView searchP(@FormParam("searchTerm") String searchTerm) {

        List<InventoryItem> ret = null;
        try {
            ret = FlipKart.search(client, searchTerm, 10);
            inventoryDAO.saveMany(ret);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView(ret);
    }

    // later
    @GET
    @Path("/load/{searchTerm}")
    @UnitOfWork
    public int loadLaptops(@PathParam("searchTerm") String searchTerm) {

        List<InventoryItem> ret = null;
        try {
            ret = FlipKart.search(client, "laptop", 10);

            inventoryDAO.saveMany(ret);
            return ret.size();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
