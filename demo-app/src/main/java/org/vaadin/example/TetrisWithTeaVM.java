package org.vaadin.example;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.parttio.tetris.TeaVMTetrisComponent;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.HorizontalFloatLayout;

@MenuItem(title = "TeaVM Tetris", icon = VaadinIcon.GAMEPAD)
@Route(layout = Layout.class)
public class TetrisWithTeaVM extends HorizontalLayout {
    private final String description = """
    TeaVM works bit like GWT, but but instead of transpiling from Java, it trasnpiles from Java bytecode.
    But no JVM, no reflection etc, so slightly more limited. Game rendered with HTML5 canvas (with TeaVM APIs)
    and controls with basic HTML buttons. In the example TeavVM classes are not on the Vaadin server's classpath,
    only its generated JS (could be WebAssembly as well) is used in the browser.
    """;

    public TetrisWithTeaVM() {
        setPadding(true);
        GameStatusDisplay gameStatusDisplay = new GameStatusDisplay(description);
        TeaVMTetrisComponent tetrisComponent = new TeaVMTetrisComponent();
        add(tetrisComponent,gameStatusDisplay);

        tetrisComponent.addGameOverListener(e -> {
            gameStatusDisplay.showGameOver(e.getScore());
        });
        tetrisComponent.addGameStateListener(e -> {
            gameStatusDisplay.updateScore(e.getScore());
        });
    }
}
