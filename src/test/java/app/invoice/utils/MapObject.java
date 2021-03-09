package app.invoice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapObject {

    public static String asJsonString(Object object) {

        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
