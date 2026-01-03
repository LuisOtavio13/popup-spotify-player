package com.exemple.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

@RestController
public class SpotifyController {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private SpotifyApi spotifyApi;

    
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(URI.create(redirectUri))
                .build();
    }

    
    @GetMapping("/login")
    public String login() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-playback-state user-modify-playback-state playlist-modify-private playlist-modify-public")
                .show_dialog(true)
                .build();

        URI uri = authorizationCodeUriRequest.execute();
        return "Acesse: " + uri.toString();
    }

    
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) throws Exception {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());
        spotifyApi.setRefreshToken(credentials.getRefreshToken());

        return "Access Token: " + credentials.getAccessToken() +
               "<br>Refresh Token: " + credentials.getRefreshToken();
    }

    
    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        if (spotifyApi == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Spotify API not initialized.");
        }
        if (spotifyApi.getRefreshToken() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No refresh token set. Please go to /login and complete the OAuth callback before requesting /token.");
        }
        try {
            AuthorizationCodeCredentials credentials = spotifyApi.authorizationCodeRefresh()
                    .build()
                    .execute();

            spotifyApi.setAccessToken(credentials.getAccessToken());
            return ResponseEntity.ok(credentials.getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to refresh token: " + e.getMessage());
        }
    }

}
