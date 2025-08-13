package edu.escuelaing.taller1;
import java.net.*;
public class App {
 public static void main(String[] args) throws MalformedURLException {
        String UrlExample = "https://www.ejemplo.com:8080/ruta/archivo.html?parametro=valor#seccion";

        URL url = new URL(UrlExample);

        // Obtener y mostrar los componentes de la URL
        System.out.println("Protocolo: " + url.getProtocol());
        System.out.println("Autoridad: " + url.getAuthority());
        System.out.println("Host: " + url.getHost());
        System.out.println("Puerto: " + url.getPort());
        System.out.println("Ruta: " + url.getPath());
        System.out.println("Consulta: " + url.getQuery());
        System.out.println("Archivo: " + url.getFile());
        System.out.println("Referencia: " + url.getRef());
    }
}