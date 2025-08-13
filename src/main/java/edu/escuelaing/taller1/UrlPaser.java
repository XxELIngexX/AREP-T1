package main.java.edu.escuelaing.taller1;

import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class UrlPaser {
    public static void main(String[] args) throws Exception {
        URL siteURL = new URL("http://www.google.com/");

        // Crea el objeto URLConnection
        URLConnection urlConnection = siteURL.openConnection();

        // Obtiene los campos del encabezado y los almacena en una estructura Map
        Map<String, List<String>> headers = urlConnection.getHeaderFields();

        // Obtiene una vista del mapa como conjunto de pares <K,V> para poder iterarlo
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();

        // Recorre la lista de campos e imprime los valores
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            // Si el nombre es nulo, significa que es la línea de estado
            if (headerName != null) {
                System.out.print(headerName + ":");
            }
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.print(value);
            }
            System.out.println("");
        }

        System.out.println("-------message-body------");
        try{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));

        // try (BufferedReader reader = new BufferedReader(new InputStreamReader(google.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
