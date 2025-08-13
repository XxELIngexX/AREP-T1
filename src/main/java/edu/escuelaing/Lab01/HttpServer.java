package edu.escuelaing.Lab01;

import java.net.*;
import java.io.*;

public class HttpServer {
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

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            
            String path = null;
            Boolean firtsLine = true;

            while ((inputLine = in.readLine()) != null) {
                if(firtsLine) {
                    try {
                        URI request = new URI(inputLine.split(" ")[1]);
                        System.out.println("@@@@ " + request.getPath());
                    } catch (URISyntaxException e) {
                        System.err.println("Invalid URI syntax: " + inputLine);
                    }
                    firtsLine= false;
                }

                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "content-type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Title of the document</title>\n"
                    + "</head>"
                    + "<body>"
                    + "My Web Site"
                    + "</body>"
                    + "</html>";
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}