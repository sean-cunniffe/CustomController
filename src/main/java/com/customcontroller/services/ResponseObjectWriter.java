package com.customcontroller.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;

/**
 * Encapsulates a object writer and a class that extends hashmap, so we can write response bodies easier
 * Created by SeanCunniffe on 05/Mar/2022
 */

@LocalBean
public class ResponseObjectWriter {

    ObjectWriter writer;

    @PostConstruct
    public void init() {
        writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public JsonBuilder createJson() {
        return new JsonBuilder(writer);
    }

}
