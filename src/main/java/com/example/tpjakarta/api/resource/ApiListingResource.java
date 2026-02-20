package com.example.tpjakarta.api.resource;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.Path;

@Path("/openapi.{type:json|yaml}")
public class ApiListingResource extends OpenApiResource {
}
