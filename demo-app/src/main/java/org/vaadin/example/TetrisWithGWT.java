package org.vaadin.example;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.parttio.tetrisgwt.GWTTetrisComponent;
import org.vaadin.firitin.appframework.MenuItem;

@MenuItem(title = "GWT Tetris", order = MenuItem.END - 1)
@Route(value = "gwt-tetris", layout = Layout.class)
public class TetrisWithGWT extends HorizontalLayout {

    final String description = """
    This Tetris implementation is compiled from Java to JavaScript using Google Web Toolkit (GWT). 
    The game logic is written in Java and runs in the browser as optimized JavaScript.
    Game state events are communicated back to the server using Vaadin's event system.
    """;

    public TetrisWithGWT() {
        setPadding(true);

        GameStatusDisplay statusDisplay = new GameStatusDisplay(description);
        GWTTetrisComponent tetris = new GWTTetrisComponent();


        tetris.addGameStateListener(e -> {
            statusDisplay.updateScore(e.getScore());
        });
        
        tetris.addGameOverListener(e -> {
            statusDisplay.showGameOver(e.getScore());
        });

        add(tetris, statusDisplay);
    }
}