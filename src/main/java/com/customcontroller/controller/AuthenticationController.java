package com.customcontroller.controller;

import com.customcontroller.dto.AuthenticationRequest;
import com.customcontroller.dto.AuthenticationResponse;
import com.customcontroller.entity.User;
import com.customcontroller.exceptions.AuthenticationException;
import com.customcontroller.repository.UserRepository;
import com.customcontroller.services.JwtUtil;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by SeanCunniffe on 16/Jan/2022
 */

@Stateless
@LocalBean
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationController {

    @Inject
    private UserRepository userRepository;

    @Inject
    private JwtUtil jwtUtil;

    @POST
    public Response getRefreshToken(@NotNull @Valid AuthenticationRequest authenticationRequest) {
        User user = null;
        try {
            user = userRepository.getUserByEmailAndPassword(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new WebApplicationException("User does not exist", Response.Status.UNAUTHORIZED);
        }
        String refreshToken = jwtUtil.createRefreshToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(user, refreshToken);
        return Response.status(Response.Status.CREATED).entity(authenticationResponse).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getAccessToken(@NotNull @Valid String refreshToken) {
        User validUser;
        try {
            String email = jwtUtil.getEmailFromToken(refreshToken);
            validUser = userRepository.getUserByEmail(email);
        }catch(Exception e){
            throw new WebApplicationException(e.getMessage(), Response.Status.UNAUTHORIZED);
        }

        String accessToken = jwtUtil.createAccessToken(validUser, refreshToken);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(validUser, accessToken);
        return Response.status(Response.Status.CREATED).entity(authenticationResponse).build();
    }
}
