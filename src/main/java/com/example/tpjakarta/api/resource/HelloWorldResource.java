package com.example.tpjakarta.api.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {

    @GET
    @Path("/helloWorld")
    public Response helloWorld() {
        return Response.ok("Hello World !").build();
    }

    @GET
    @Path("/params/{pathParam}")
    public Response params(@PathParam("pathParam") String pathParam, @QueryParam("queryParam") String queryParam) {
        String response = String.format("Path param: %s, Query param: %s", pathParam, queryParam);
        return Response.ok(response).build();
    }
}
