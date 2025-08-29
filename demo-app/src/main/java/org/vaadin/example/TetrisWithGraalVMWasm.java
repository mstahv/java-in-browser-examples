package org.vaadin.example;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.parttio.tetris.GraalVmTetrisComponent;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.util.ResizeObserver;

@MenuItem(title = "GraalVM Tetris", icon = VaadinIcon.GAMEPAD, order = MenuItem.END)
@Route(layout = Layout.class)
public class TetrisWithGraalVMWasm extends HorizontalLayout {
    private final String description = """
    GraalVM's experimental WebAssembly (Wasm) support allows Java code to be compiled into WebAssembly modules that can run in web browsers.
    Similarish limitations as with TeaVM and GWT apply.
    """;

    public TetrisWithGraalVMWasm() {
        setPadding(true);
        var gameStatusDisplay = new GameStatusDisplay(description);
        var tetrisComponent = new GraalVmTetrisComponent();
        add(tetrisComponent, gameStatusDisplay);

        tetrisComponent.addGameOverListener(e -> {
            gameStatusDisplay.showGameOver(e.getScore());
        });
        tetrisComponent.addGameStateListener(e -> {
            gameStatusDisplay.updateScore(e.getScore());
        });

    }
}
