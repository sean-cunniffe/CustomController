package com.customcontroller.controller;

import com.customcontroller.entity.order.Controller;
import com.customcontroller.entity.order.ControllerType;
import com.customcontroller.repository.ControllerRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by SeanCunniffe on 08/Mar/2022
 */
@Stateless
@LocalBean
@Path("/controller")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ControllersController {

    @Inject
    ControllerRepository controllerRepository;

    @GET
    public Response getAllControllers(){
        List<Controller> controllers = controllerRepository.findAll();
        return Response.ok().entity(controllers).build();
    }

    @GET
    @Path("/promo")
    public Response getPromoControllers(@QueryParam("count") @Min(0) Integer count){
        List<Controller> randomControllers = controllerRepository.getRandomControllers(count);
        return Response.ok().entity(randomControllers).build();
    }

    @GET
    @Path("/types")
    public Response getAllControllerTypes(){
        List<ControllerType> controllerTypes = controllerRepository.getAllControllerTypes();
        return Response.ok().entity(controllerTypes).build();
    }
}
