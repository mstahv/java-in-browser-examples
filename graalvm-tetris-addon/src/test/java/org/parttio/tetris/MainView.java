package org.parttio.tetris;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.vaadin.firitin.components.notification.VNotification;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

@Route
public class MainView extends VVerticalLayout {
    public MainView() {
        H1 h1 = new H1("Client-side Tetris with Vaadin Flow");
        add(h1);
        var tetris = new GraalVmTetrisComponent();
        add(tetris);

        tetris.addGameOverListener(e -> {
            VNotification.prominent("Game Over! Final score: " + e.getScore());
        });

        tetris.addGameStateListener(e -> {
            Notification.show("Score now: " + e.getScore()).setPosition(Notification.Position.TOP_END);
        });

    }
}
