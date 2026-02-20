package com.example.tpjakarta.api.resource;

import com.example.tpjakarta.api.dto.AnnonceCreateDTO;
import com.example.tpjakarta.api.dto.AnnonceDTO;
import com.example.tpjakarta.beans.User;
import com.example.tpjakarta.services.AnnonceService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/annonces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnonceResource {

    private final AnnonceService annonceService;

    public AnnonceResource() {
        this.annonceService = new AnnonceService();
    }

    private Long getAuthenticatedUserId(SecurityContext securityContext) {
        if (securityContext != null && securityContext.getUserPrincipal() != null) {
            try {
                return Long.parseLong(securityContext.getUserPrincipal().getName());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("1") int page,
                           @QueryParam("size") @DefaultValue("10") int size) {
        List<AnnonceDTO> annonces = annonceService.findAll(page, size);
        return Response.ok(annonces).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        // Service throws ResourceNotFoundException if not found -> 404
        AnnonceDTO annonce = annonceService.findById(id);
        return Response.ok(annonce).build();
    }

    @POST
    public Response create(@jakarta.validation.Valid AnnonceCreateDTO dto, @Context SecurityContext securityContext) {
        Long userId = getAuthenticatedUserId(securityContext);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        AnnonceDTO created = annonceService.create(dto, userId);
        return Response.created(URI.create("/api/annonces/" + created.getId())).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @jakarta.validation.Valid AnnonceCreateDTO dto, @Context SecurityContext securityContext) {
        Long userId = getAuthenticatedUserId(securityContext);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.update(id, dto, userId);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Long userId = getAuthenticatedUserId(securityContext);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        annonceService.delete(id, userId);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    public Response patch(@PathParam("id") Long id, Map<String, Object> updates, @Context SecurityContext securityContext) {
        Long userId = getAuthenticatedUserId(securityContext);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        AnnonceDTO updated = annonceService.patch(id, updates, userId);
        return Response.ok(updated).build();
    }
}
