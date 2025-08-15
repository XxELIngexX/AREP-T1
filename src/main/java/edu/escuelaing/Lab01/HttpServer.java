package edu.escuelaing.Lab01;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.*;

public class HttpServer {

    private static final String localPath = "src/main/resources/public_html";

    private static String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        }
        return "text/plain";
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            System.out.println("Servidor iniciado en el puerto 35000...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                }
            }
        }
    }

    public static void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8),
                true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        String path = null;
        String method = null;
        boolean firstLine = true;
        String contentLengthStr = null;

        // Leer encabezados HTTP
        while ((inputLine = in.readLine()) != null) {
            if (firstLine) {
                try {
                    URI request = new URI(inputLine.split(" ")[1]);
                    path = request.getPath();
                    method = inputLine.split(" ")[0];
                    System.out.println("Method: " + method + " | Path: " + path);
                } catch (URISyntaxException e) {
                    System.err.println("Invalid URI syntax: " + inputLine);
                }
                firstLine = false;
            }
            if (inputLine.startsWith("Content-Length:")) {
                contentLengthStr = inputLine.split(": ")[1];
            }
            if (inputLine.isEmpty()) {
                break;
            }
        }

        // Procesar petición POST
        if ("POST".equalsIgnoreCase(method) && path.startsWith("/options")) {
            if (contentLengthStr != null) {
                int contentLength = Integer.parseInt(contentLengthStr);
                char[] body = new char[contentLength];
                in.read(body, 0, contentLength);
                String bodyContent = new String(body);
                System.out.println("POST body-in: " + bodyContent);
            }

            OutputStream rawOut = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 2\r\n" +
                    "\r\n" +
                    "OK";
            rawOut.write(response.getBytes(StandardCharsets.UTF_8));
            rawOut.flush();

        } else {
            // Procesar petición GET
            String filePath = localPath + path;
            if (path.equals("/")) {
                filePath = localPath + "/index.html";
            }
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(filePath);
                System.out.println("Content type: " + contentType);

                if (contentType.startsWith("image")) {
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    OutputStream rawOut = clientSocket.getOutputStream();
                    String header = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType + "\r\n" +
                            "Content-Length: " + fileData.length + "\r\n" +
                            "\r\n";
                    rawOut.write(header.getBytes(StandardCharsets.UTF_8));
                    rawOut.write(fileData);
                    rawOut.flush();
                } else {
                    String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                    String header = "HTTP/1.1 200 OK\r\n"
                            + "content-type: " + contentType + "; charset=UTF-8\r\n"
                            + "\r\n";
                    out.println(header + content);
                }

            } else {
                // Recurso no encontrado
                String notFound = "<html><body><h1>404 Not Found</h1></body></html>";
                String header = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + notFound.length() + "\r\n" +
                        "\r\n";
                out.println(header + notFound);
            }
        }

        out.close();
        in.close();
        clientSocket.close();
    }
}
