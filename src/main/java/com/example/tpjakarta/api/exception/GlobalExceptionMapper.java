package com.example.tpjakarta.api.exception;

import com.example.tpjakarta.api.dto.ErrorDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ResourceNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorDTO(404, "Not Found", exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else if (exception instanceof BusinessException) {
            // Mapping BusinessException to 409 Conflict as requested/planned
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorDTO(409, "Business Conflict", exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else if (exception instanceof SecurityException) {
            // Mapping SecurityException to 403 Forbidden
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorDTO(403, "Forbidden", exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else if (exception instanceof jakarta.persistence.OptimisticLockException) {
            // Mapping OptimisticLockException to 409 Conflict
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorDTO(409, "Optimistic Lock Exception", "The resource has been modified by another transaction."))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        
        // Default 500
        exception.printStackTrace(); // Log the error
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDTO(500, "Internal Server Error", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
