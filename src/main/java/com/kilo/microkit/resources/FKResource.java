package com.kilo.microkit.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by kraghunathan on 9/16/16.
 */
@Path("/feeds")
@Produces(MediaType.APPLICATION_JSON)
public class FKResource {

    private final Client client;

    public FKResource(Client client) {
        this.client = client;
    }

    @GET
    @Path("/hello")
    public String hello() {

        return "Hello + " + new Date();
    }

}
