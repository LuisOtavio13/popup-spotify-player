package com.exemple.demo;

import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Component
public class UIStarter {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        new Thread(() -> PopupSpotify.startUI(), "UI-Starter").start();
    }
}
