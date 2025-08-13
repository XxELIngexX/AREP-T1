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
        return "text/plain"; // Default content type
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while (running) {

            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // forzar el UTF_8
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            // procesar la peticion
            // lectura
            String path = null;
            Boolean firtsLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (firtsLine) {
                    try {
                        URI request = new URI(inputLine.split(" ")[1]);
                        path = request.getPath();
                        System.out.println("path solicitado: " + path);
                    } catch (URISyntaxException e) {
                        System.err.println("Invalid URI syntax: " + inputLine);
                    }
                    firtsLine = false;
                }

                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            // Generar respuesta
            String filePath = localPath + path;
            if (path.equals("/")) {
                filePath = localPath + "/index.html";
            } else {

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
                String notFound = "<html><body><h1>404 Not Found</h1></body></html>";
                String header = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + notFound.length() + "\r\n" +
                        "\r\n";
                out.println(header + notFound);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}