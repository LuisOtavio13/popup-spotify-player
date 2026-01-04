package com.example.ui;

import com.example.ui.serviceBackEnd.BackendClient;
import javafx.application.Platform;

public class SpotifyPoller {

    public static void start(SpotifyUIUpdater updater) {

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    String token = BackendClient.getAccessToken();

                    if (token == null) {
                        BackendClient.openLogin();
                        Thread.sleep(10000);
                        continue;
                    }

                    String json = HelloController.getCurrentlyPlaying(token);
                    if (json != null) {
                        Platform.runLater(() -> updater.update(json));
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
                }
            }
        }, "Spotify-Poller");

        t.setDaemon(true);
        t.start();
    }
}



