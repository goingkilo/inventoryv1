package com.kilo.microkit.resources;

import com.kilo.microkit.api.APIFeeds;
import com.kilo.microkit.api.AffiliateAPIException;
import com.kilo.microkit.api.dao.InventoryDAO;
import com.kilo.microkit.api.model.InventoryItem;
import com.kilo.microkit.api.util.FlipKart;
import com.kilo.microkit.views.ItemsView;
import io.dropwizard.hibernate.UnitOfWork;

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

    public InventoryResource(InventoryDAO inventoryDAO, Client client) {
        this.inventoryDAO = inventoryDAO;
        this.client = client;
    }

    @GET
    @Path("/search/{searchTerm}")
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
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView( ret );
    }

    @GET
    @Path("/categories")
    @UnitOfWork
    public String[] get() {

        APIFeeds feeds = new APIFeeds("goingkilo", "1368e5baaf8e4bcdb442873d4aa8ef6e", "no");
        try {

            Map<String, String> categories = feeds.categories();
            String[] ret = categories.keySet().toArray( new String[]{});
            Arrays.sort(ret);
            return  ret;

        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        }
        return new String[]{};
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
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView( ret );
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
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
