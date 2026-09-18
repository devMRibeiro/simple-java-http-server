package br.com.devmribeiro.taskapi;

import java.io.IOException;

import br.com.devmribeiro.taskapi.config.Log;
import br.com.devmribeiro.taskapi.config.PropertiesFileConfig;
import br.com.devmribeiro.taskapi.exception.GlobalExceptionHandler;
import br.com.devmribeiro.taskapi.http.server.Server;

public class TaskApp {
	public static void main(String[] args) throws IOException {
        
        Integer port = PropertiesFileConfig.getValueAsInteger("PORT");
        
        if (port == null) {
        	port = 8080;
        	Log.w("No port configuration found. Port set to {0}", port);
        }
        
        Server server = new Server();
        server.start(port, new GlobalExceptionHandler());
    }
}