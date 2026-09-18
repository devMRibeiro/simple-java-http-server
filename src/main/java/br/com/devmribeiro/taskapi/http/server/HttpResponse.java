package br.com.devmribeiro.taskapi.http.server;

import java.util.Map;

public class HttpResponse {

    private final HttpStatus status;
    private final Map<String, String> headers;
    private final String body;

	public HttpResponse(
    		HttpStatus status,
            Map<String, String> headers,
            String body) {

        this.status = status;
        this.headers = headers;
        this.body = body;
    }
    
    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}