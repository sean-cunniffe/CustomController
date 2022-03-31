package com.customcontroller.services;


import com.customcontroller.entity.ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthFilterTest {

    private AuthFilter authFilter;

    JwtUtil jwtUtil;

    ContainerRequestContext requestContext;

    @BeforeEach
    public void beforeEach() {
        jwtUtil = mock(JwtUtil.class);
        authFilter = new AuthFilter();
        authFilter.setJwtUtil(jwtUtil);
        authFilter.init();
        requestContext = mock(ContainerRequestContext.class);
    }

    @Test
    void testStaffAccess() throws IOException {
        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn("/users");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getMethod()).thenReturn("POST");

        MultivaluedMap<String, String> headers = mock(MultivaluedMap.class);
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer token");
        when(headers.get(eq("authorization"))).thenReturn(authorization);
        when(requestContext.getHeaders()).thenReturn(headers);

        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);
        when(jwtUtil.getRoleFromToken(any())).thenReturn(ROLE.STAFF);
        authFilter.filter(requestContext);
        verify(requestContext, new Times(0)).abortWith(any());
    }

    @Test
    void testCustomerAccess() throws IOException {
        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn("/users/1");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getMethod()).thenReturn("GET");

        MultivaluedMap<String, String> headers = mock(MultivaluedMap.class);
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer token");
        when(headers.get(eq("authorization"))).thenReturn(authorization);
        when(requestContext.getHeaders()).thenReturn(headers);

        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);
        when(jwtUtil.getRoleFromToken(any())).thenReturn(ROLE.CUSTOMER);
        authFilter.filter(requestContext);
        verify(requestContext, new Times(0)).abortWith(any());
    }

    @Test
    void testCustomerUnAuthorized() {
        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn("/user/1");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getMethod()).thenReturn("POST");

        MultivaluedMap<String, String> headers = mock(MultivaluedMap.class);
        List<String> authorization = new ArrayList<>();
        authorization.add("Bearer token");
        when(headers.get(eq("authorization"))).thenReturn(authorization);
        when(requestContext.getHeaders()).thenReturn(headers);

        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);
        when(jwtUtil.getRoleFromToken(any())).thenReturn(ROLE.CUSTOMER);
        WebApplicationException webApplicationException = assertThrows(WebApplicationException.class, () -> authFilter.filter(requestContext));
        assertEquals("Unauthorized user type", webApplicationException.getMessage());
    }

    @Test
    void testSecurityContext() {
        SecurityContext securityContext = authFilter.getSecurityContext("sean@gmail.com", ROLE.STAFF);
        assertTrue(securityContext.isUserInRole(ROLE.STAFF.toString()));
        assertTrue(securityContext.isSecure());
        assertEquals("JWT", securityContext.getAuthenticationScheme());
    }

    @Test
    void testCustomerUnAuthorizedByNotHavingHeader() {
        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn("/user/1");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(requestContext.getMethod()).thenReturn("POST");

        when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);
        when(jwtUtil.getRoleFromToken(any())).thenReturn(ROLE.CUSTOMER);
        WebApplicationException webApplicationException = assertThrows(WebApplicationException.class, () -> authFilter.filter(requestContext));
        assertEquals("no authentication header", webApplicationException.getMessage());
    }

}
