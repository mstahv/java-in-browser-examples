package org.vaadin.example;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.parttio.tetris.CheerpJTetrisComponent;
import org.vaadin.firitin.appframework.MenuItem;

@MenuItem(title = "CheerpJ Tetris", icon = VaadinIcon.GAMEPAD)
@Route(layout = Layout.class)
public class TetrisWithCheerpJ extends HorizontalLayout {
    private final String description = """
    In this example, the Tetris game is running in the browser using CheerpJ technology that 
    runs a custom JVM in the browser (using WebAssembly, coded in C++ like standard OpenJDK HotSpot) 
    and executing a standard Swing based tetris game. True nerds can steal the tetris.jar file with 
    browsers inspector and run (`java -jar tetris.jar`) it locally with standard JVM (but without the Vaadin "chrome" naturally).
    """;

    public TetrisWithCheerpJ() {
        setPadding(true);
        var status = new GameStatusDisplay(description);
        var tetrisComponent = new CheerpJTetrisComponent();
        add(tetrisComponent, status);

        tetrisComponent.addGameOverListener(e -> {
            status.showGameOver(e.getScore());
        });
        tetrisComponent.addGameStateListener(e -> {
            status.updateScore(e.getScore());
        });
    }
}
