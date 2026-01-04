package com.example.ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloController implements SpotifyUIUpdater {
    @FXML
    private ImageView albumImage;

    @FXML
    private ImageView spotifyLogo;

    @FXML
    private Label trackTitle;

    @FXML
    private Label trackInfo;

    @FXML
    private Label trackTime;

    // Guardar último estado
    private String lastTrackId = null;
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private int lastProgressMs = -1;

    @Override
    public void update(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject item = root.getAsJsonObject("item");
            String trackId = item.get("id").getAsString();
            String name = item.get("name").getAsString();
            String artist = item.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
            String albumUrl = item.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url")
                    .getAsString();
            int progressMs = root.get("progress_ms").getAsInt();
            int durationMs = item.get("duration_ms").getAsInt(); // Só atualiza se a música mudou
            if (!trackId.equals(lastTrackId)) {
                trackTitle.setText(name);
                trackInfo.setText(artist);
                albumImage.setImage(new Image(albumUrl, 120, 120, false, true, true));
                lastTrackId = trackId;
            } // Atualiza tempo apenas se mudou
            if (progressMs != lastProgressMs) {
                trackTime.setText(formatTime(progressMs) + " / " + formatTime(durationMs));
                lastProgressMs = progressMs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTime(int ms) {
        int totalSeconds = ms / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @FXML
    public void initialize() {
        albumImage.setFitWidth(60);
        albumImage.setFitHeight(60);
        albumImage.setPreserveRatio(false);
        albumImage.setSmooth(true);
        albumImage.setCache(true);
        albumImage.setCacheHint(javafx.scene.CacheHint.QUALITY);
        Rectangle clip = new Rectangle(60, 60);
        clip.setArcWidth(14);
        clip.setArcHeight(14);
        albumImage.setClip(clip);
        spotifyLogo.setImage(new Image(
                getClass().getResource("/com/example/ui/eu2.png").toExternalForm(), 36, 36,
                true, true));
    }

    public static String getCurrentlyPlaying(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/me/player/currently-playing"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204 || response.statusCode() == 401)
            return null;
        System.out.println(response.body());
        return response.body();
    }
}
