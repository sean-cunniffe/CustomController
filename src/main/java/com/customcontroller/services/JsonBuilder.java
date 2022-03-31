package com.customcontroller.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SeanCunniffe on 05/Mar/2022
 */

public class JsonBuilder {

    ObjectWriter writer;

    private final Map<Object, Object> map;

    public JsonBuilder(ObjectWriter writer) {
        this.writer = writer;
        map = new HashMap<>();
    }

    public JsonBuilder put(Object key, Object value){
        map.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        try {
            return writer.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
