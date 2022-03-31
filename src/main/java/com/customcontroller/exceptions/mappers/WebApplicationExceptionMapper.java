package com.customcontroller.exceptions.mappers;

import com.customcontroller.services.ResponseObjectWriter;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by SeanCunniffe on 26/Feb/2022
 */

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Inject
    ResponseObjectWriter objectWriter;

    @Override
    public Response toResponse(WebApplicationException exception) {
        return Response
                .status(exception.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(objectWriter.createJson().put("error", exception.getLocalizedMessage()).toString())
                .build();
    }
    public void setObjectWriter(ResponseObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }
}
