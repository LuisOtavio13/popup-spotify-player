package com.exemple.demo;

import javax.swing.*;

import com.exemple.demo.utils.Request2;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PopupSpotify {
    public static void main(String[] args) {
        startUI();
    }

    public static void startUI() {
        
        new Thread(() -> {
            while (true) {
                try {
                    String accessToken = Request2.getAccessTokenFromSpring();
                    if (accessToken == null || accessToken.isBlank()) {
                        System.out.println("No access token yet");
                        Thread.sleep(5000);
                        continue;
                    }
                    System.out.println("Got access token (masked): " + (accessToken.length() > 10 ? accessToken.substring(0,10) + "..." : accessToken));
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.spotify.com/v1/me/player/currently-playing"))
                            .header("Authorization", "Bearer " + accessToken)
                            .GET()
                            .build();

                    HttpClient client = HttpClient.newHttpClient();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        System.out.println(response.body());
                    }

                    Thread.sleep(5000);
                } catch (Exception e) {
                    
                    e.printStackTrace();
                    try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
                }
            }
        }, "PopupSpotify-Poller").start();

        
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Headless environment detected, UI will not be started.");
        } else {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("java");
                frame.setSize(250, 120);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setUndecorated(true);
                frame.setAlwaysOnTop(true);

                JPanel panel = new JPanel();
                panel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
                panel.setBackground(new Color(30, 215, 96));
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel titulo = new JLabel("Tocando agora");
                titulo.setForeground(Color.WHITE);
                titulo.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel musica = new JLabel("Nome da música");
                musica.setForeground(Color.WHITE);

                JLabel artista = new JLabel("Artista");
                artista.setForeground(Color.LIGHT_GRAY);

                panel.add(titulo);
                panel.add(musica);
                panel.add(artista);

                frame.add(panel);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = screenSize.width - frame.getWidth() - 20;
                int y = screenSize.height - frame.getHeight() - 40;
                frame.setLocation(x, y);
                final Point clickPoint = new Point();

                frame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        clickPoint.x = e.getX();
                        clickPoint.y = e.getY();
                    }
                });
                frame.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        int newX = frame.getX() + e.getX() - clickPoint.x;
                        int newY = frame.getY() + e.getY() - clickPoint.y;
                        frame.setLocation(newX, newY);
                    }
                });
                frame.setVisible(true);
            });
        }
    }
}