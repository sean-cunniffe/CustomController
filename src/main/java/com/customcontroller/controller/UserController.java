package com.customcontroller.controller;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.factory.UserFactory;
import com.customcontroller.repository.UserRepository;
import com.customcontroller.services.ResponseObjectWriter;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SeanCunniffe on 26/Feb/2022
 */


@Stateless
@LocalBean
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserFactory userFactory;

    @Inject
    private ResponseObjectWriter objectWriter;


    @POST
    public Response createAccount(@Valid @NotNull User userData, @Context SecurityContext securityContext) {
        if (userRepository.getUserByEmail(userData.getEmail()) != null) {
            throw new WebApplicationException("User already exists", Response.Status.CONFLICT);
        }
        if (securityContext == null || !securityContext.isUserInRole(ROLE.STAFF.name()) || userData.getRole() == null)
            userData.setRole(ROLE.CUSTOMER);
        User user = userFactory.createUser(userData.getEmail(), userData.getFirstName(), userData.getLastName(), userData.getPassword(), userData.getRole());
        user = userRepository.add(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    /**
     * returns the user if the email in the securityContext is the same as the query or the security context role is staff
     *
     * @param email           The email of the user to get
     * @param securityContext the security context of the request token
     * @return a response with 200 if passes security check, else 403
     */
    @GET
    public Response getUserByEmail(@QueryParam(value = "email") String email, @Context SecurityContext securityContext) {
        String requestEmail = securityContext.getUserPrincipal().getName();
        if (!requestEmail.equals(email) && !securityContext.isUserInRole(ROLE.STAFF.name())) {
            throw new WebApplicationException("Unauthorized request", 403);
        }
        User user = userRepository.getUserByEmail(email);
        if (user == null)
            throw new WebApplicationException("User not found", Response.Status.BAD_REQUEST);

        return Response.ok().entity(user).build();
    }

}
