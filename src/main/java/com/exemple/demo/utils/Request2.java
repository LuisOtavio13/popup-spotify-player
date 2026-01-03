package com.exemple.demo.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request2 {
    final static String URL = "http://127.0.0.1:8080/token";

    public static String getAccessTokenFromSpring()  {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create((URL)))
            .GET()
            .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    try{
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }catch(Exception e){
        e.printStackTrace();
        return null;
    }
   
    
}

}
