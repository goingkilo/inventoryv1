package com.kilo.microkit.resources;

import com.kilo.microkit.api.dao.InventoryDAO;
import com.kilo.microkit.api.model.InventoryItem;
import com.kilo.microkit.api.util.FlipKart;
import com.kilo.microkit.views.HelloView;
import com.kilo.microkit.views.ItemsView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.List;

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
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    public HelloView hello() {

        return new HelloView("Dis mesg");
    }

    @GET
    @Path("/s/{searchTerm}")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public ItemsView search(@PathParam("searchTerm") String searchTerm) {

        if( searchTerm == null ) searchTerm = "laptop";

        List<InventoryItem> ret = null;
        try {
            ret = inventoryDAO.getInventoryItems();

            if( ret == null || ret.size() == 0  ) {
                ret = FlipKart.search(client, searchTerm, 10);
                inventoryDAO.saveMany(ret);
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return new ItemsView( ret );
    }

    @POST
    @Path("/s1")
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
    public int load(@PathParam("searchTerm") String searchTerm) {

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
