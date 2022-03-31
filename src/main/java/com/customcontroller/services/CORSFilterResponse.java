package com.customcontroller.services;


import javax.annotation.PostConstruct;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class CORSFilterResponse implements ContainerResponseFilter {

    private List<Object> accessControl;
    private ArrayList<Object> allowedCredentials;
    private ArrayList<Object> allowedHeaders;
    private ArrayList<Object> allowedMethods;
    private ArrayList<Object> maxAge;


    @PostConstruct
    void init(){
        accessControl = new ArrayList<>();
        accessControl.add("*");

        allowedCredentials = new ArrayList<>();
        allowedCredentials.add("true");

        allowedHeaders = new ArrayList<>();
        allowedHeaders.add("origin, content-type, accept, authorization");

        allowedMethods = new ArrayList<>();
        allowedMethods.add("POST, GET, PUT, DELETE, OPTIONS, HEAD");

        maxAge = new ArrayList<>();
        maxAge.add("86400");
    }


    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.put(
                "Access-Control-Allow-Origin", accessControl);
        responseContext.getHeaders().put(
                "Access-Control-Allow-Credentials", allowedCredentials);
        responseContext.getHeaders().put(
                "Access-Control-Allow-Headers",
                allowedHeaders);
        responseContext.getHeaders().put(
                "Access-Control-Allow-Methods",
                allowedMethods);
        responseContext.getHeaders().put("Access-Control-Max-Age", maxAge);
        if (requestContext.getMethod().equals("OPTIONS")) {
            responseContext.setStatus(204);
        }
    }
}
