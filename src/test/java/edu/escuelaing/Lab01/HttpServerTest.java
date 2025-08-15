package edu.escuelaing.Lab01;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class HttpServerTest {

    @Test
    public void testHandleGetRequest() throws IOException {
        // Simular una petición GET a /layouts/menu.html
        String fakeRequest = "GET /layouts/menu.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        ByteArrayInputStream input = new ByteArrayInputStream(fakeRequest.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Socket falso
        Socket fakeSocket = new FakeSocket(input, output);

        // Llamar al método que maneja la solicitud
        HttpServer.handleRequest(fakeSocket);

        String response = output.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK")); // Verifica que responde OK
    }

    @Test
    public void testHandleNotFoundRequest() throws IOException {
        // Simular una petición GET a un recurso que no existe
        String fakeRequest = "GET /noexiste.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        ByteArrayInputStream input = new ByteArrayInputStream(fakeRequest.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Socket fakeSocket = new FakeSocket(input, output);
        HttpServer.handleRequest(fakeSocket);

        String response = output.toString();
        assertTrue(response.contains("404")); // Verifica que es error 404
    }

    @Test
    public void testHandlePostRequest() throws IOException {
        // Simular una petición POST a /options con un nombre
        String fakeRequest =
                "POST /options HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Length: 20\r\n\r\n" +
                "{\"name\":\"JUnitUser\"}";

        ByteArrayInputStream input = new ByteArrayInputStream(fakeRequest.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Socket fakeSocket = new FakeSocket(input, output);
        HttpServer.handleRequest(fakeSocket);

        String response = output.toString();
        assertTrue(response.contains("OK")); // Verifica que la respuesta incluye el nombre
    }

    // Clase Socket falsa para simular conexiones
    static class FakeSocket extends Socket {
        private final InputStream input;
        private final OutputStream output;

        FakeSocket(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public InputStream getInputStream() {
            return input;
        }

        @Override
        public OutputStream getOutputStream() {
            return output;
        }

        @Override
        public synchronized void close() {
            // No hacer nada
        }
    }
}
