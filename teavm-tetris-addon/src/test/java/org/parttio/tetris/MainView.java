package org.parttio.tetris;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

@Route
public class MainView extends VVerticalLayout {
    public MainView() {
        H1 h1 = new H1("Client-side Tetris with Vaadin Flow");
        add(h1);
        var tetris = new TetrisComponent();
        add(tetris);

        tetris.addGameOverListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Game Over");
            dialog.add("Game Over! Your score: " + e.getScore());
            dialog.open();
        });
        tetris.addGameStateListener(e -> {
           h1.setText("Client-side Tetris with Vaadin Flow, current score: " + e.getScore());
        });

    }
}
