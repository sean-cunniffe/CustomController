package com.customcontroller.services;


import org.junit.jupiter.api.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CORSFilterResponseTest {

    CORSFilterResponse corsFilterResponse;

    @Test
    void testHeadersAreAdded() {
        corsFilterResponse = new CORSFilterResponse();
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        when(responseContext.getHeaders()).thenReturn(headers);
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getMethod()).thenReturn("OPTIONS");
        corsFilterResponse.init();
        corsFilterResponse.filter(
                requestContext, responseContext);
        assertEquals("*", headers.get("Access-Control-Allow-Origin").get(0));
        assertEquals("true", headers.get("Access-Control-Allow-Credentials").get(0));
        assertEquals("origin, content-type, accept, authorization",
                headers.get("Access-Control-Allow-Headers").get(0));
        assertEquals("POST, GET, PUT, DELETE, OPTIONS, HEAD",
                headers.get("Access-Control-Allow-Methods").get(0));
        assertEquals("86400", headers.get("Access-Control-Max-Age").get(0));

    }

}
