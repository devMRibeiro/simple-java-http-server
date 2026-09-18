package br.com.devmribeiro.taskapi.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;

import br.com.devmribeiro.taskapi.config.Log;
import br.com.devmribeiro.taskapi.controller.TaskController;
import br.com.devmribeiro.taskapi.exception.GlobalExceptionHandler;
import br.com.devmribeiro.taskapi.http.router.Router;
import br.com.devmribeiro.taskapi.repository.TaskRepository;
import br.com.devmribeiro.taskapi.service.TaskService;

public class Server {

    public void start(int port, GlobalExceptionHandler exceptionHandler) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        TaskRepository taskRepository = new TaskRepository();
        TaskService taskService = new TaskService(taskRepository);
        TaskController taskController = new TaskController(taskService);
        
        Router router = new Router(taskController);

        server.createContext("/", exchange -> {

        	try {
        		HttpResponse response = router.handle(exchange);

        		byte[] body = new byte[0];
        		if (response.getBody() != null)
        			body = response.getBody().getBytes(StandardCharsets.UTF_8);
        		
        		exchange.getResponseHeaders().set("Content-Type", response.getHeaders().get("Content-Type"));
        		exchange.sendResponseHeaders(response.getStatus().getCode(), body.length);
        		exchange.getResponseBody().write(body);
        		exchange.getResponseBody().close();
			} catch (Exception e) {
				exceptionHandler.handle(e, exchange);
			}
        });

        server.start();

        Log.i("Server running on port " + port);
    }
}