package com.customcontroller.exceptions.mappers;

import com.customcontroller.services.ResponseObjectWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebApplicationExceptionMapperTest {

    WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();

    @BeforeAll
    void init(){
        ResponseObjectWriter objectWriter = new ResponseObjectWriter();
        objectWriter.init();
        webApplicationExceptionMapper.setObjectWriter(objectWriter);
    }

    @Test
    void testResponseToException(){
        WebApplicationException webApplicationException = new WebApplicationException("No auth", 403);
        Response response = webApplicationExceptionMapper.toResponse(webApplicationException);
        assertEquals(403, response.getStatus());
    }


}
