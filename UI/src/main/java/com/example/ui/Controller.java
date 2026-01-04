package com.example.ui;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {

    @FXML
    private Label trackTitle;

    @FXML
    private Label artist;

    @FXML
    private ImageView albumImage;

    private void updateUI(String json) {

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonObject item = root.getAsJsonObject("item");

        if (item == null) return;

        String name = item.get("name").getAsString();

        String artistName = item.getAsJsonArray("artists")
                .get(0).getAsJsonObject()
                .get("name").getAsString();

        String imageUrl = item.getAsJsonObject("album")
                .getAsJsonArray("images")
                .get(0).getAsJsonObject()
                .get("url").getAsString();

        trackTitle.setText(name);
        artist.setText(artistName);
        albumImage.setImage(new Image(imageUrl, true));
    }
}
