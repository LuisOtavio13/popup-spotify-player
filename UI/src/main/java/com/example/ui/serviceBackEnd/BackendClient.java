package com.example.ui.serviceBackEnd;

import java.awt.*;
import java.net.URI;
import java.net.http.*;

public class BackendClient {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String BASE_URL = "http://localhost:8080";

    public static String getAccessToken() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/token"))
                .GET()
                .build();

        HttpResponse<String> response =
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        }
        return response.body();
    }

    public static void openLogin() throws Exception {
        Desktop.getDesktop().browse(
                URI.create(BASE_URL + "/login")
        );
    }
}
