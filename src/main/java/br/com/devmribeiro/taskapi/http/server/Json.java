package br.com.devmribeiro.taskapi.http.server;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.devmribeiro.taskapi.config.Log;

public class Json {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T fromJson(InputStream input, Class<T> type) {
        try {
            return MAPPER.readValue(input, type);
        } catch (IOException e) {
        	Log.e("Erro ao converter deserializar", e);
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object object) {
    	
    	try {
    		return MAPPER.writeValueAsString(object);
    	} catch (JsonProcessingException e) {
    		throw new RuntimeException(e);
    	}
    }
}