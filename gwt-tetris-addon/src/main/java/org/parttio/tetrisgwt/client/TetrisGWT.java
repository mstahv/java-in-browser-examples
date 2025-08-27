package org.parttio.tetrisgwt.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.parttio.example.tetrislib.Game;
import org.parttio.example.tetrislib.Grid;

public class TetrisGWT implements EntryPoint {

    private static final int CELL_SIZE = 30;
    private static final int GAME_WIDTH = 10;
    private static final int GAME_HEIGHT = 20;

    private Canvas canvas;
    private Context2d context;
    private Game game;
    private Timer gameTimer;
    private boolean paused = false;
    private Element element;

    public static final native void exportMyFunction() /*-{
       $wnd.startGwtTetris = $entry(function(element) {
        @org.parttio.tetrisgwt.client.TetrisGWT::start(Lcom/google/gwt/dom/client/Element;)(element);
       });
       if ($wnd.tetrisElement) {
           $wnd.startGwtTetris($wnd.tetrisElement);
           $wnd.tetrisElement = null;
       }
    }-*/;

    public static final void start(Element element) {
        TetrisGWT tetris = new TetrisGWT();
        tetris.startOnElement(element);
    }

    public final void startOnElement(Element element) {
        this.element = element;
        HTMLPanel container = HTMLPanel.wrap(element);

        // Initialize the game
        game = new Game(GAME_WIDTH, GAME_HEIGHT);

        // Create canvas
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            GWT.log("Canvas not supported");
            return;
        }

        canvas.setWidth(GAME_WIDTH * CELL_SIZE + "px");
        canvas.setHeight(GAME_HEIGHT * CELL_SIZE + "px");
        canvas.setCoordinateSpaceWidth(GAME_WIDTH * CELL_SIZE);
        canvas.setCoordinateSpaceHeight(GAME_HEIGHT * CELL_SIZE);

        context = canvas.getContext2d();

        // Set up keyboard controls

        canvas.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                handleKeyPress(event.getNativeKeyCode());
                event.preventDefault();
            }
        });

        HorizontalPanel controls = new HorizontalPanel();
        container.add(controls);

        Button left = new Button("⬅️");
        left.addClickHandler(clickEvent -> {
            game.moveLeft();
            render();
        });

        Button right = new Button("➡️");
        right.addClickHandler(clickEvent -> {
            game.moveRight();
            render();
        });
        Button down = new Button("⬇️");
        down.addClickHandler(clickEvent -> {
            game.drop();
            render();
        });
        Button rotate = new Button("🔄");
        rotate.addClickHandler(clickEvent -> {
            game.rotateCW();
            render();
        });
        controls.add(left);
        controls.add(right);
        controls.add(down);
        controls.add(rotate);

        container.add(canvas);

        canvas.setFocus(true);

        // Start game loop
        startGameLoop();

        // Initial render
        render();

        // Fire initial game state event
        fireGameStateEvent(element, game.getScore(), game.isOver());
    }

    public void onModuleLoad() {
        exportMyFunction();
    }

    private void handleKeyPress(int keyCode) {
        if (game.isOver() || paused) {
            return;
        }

        try {
            switch (keyCode) {
                case KeyCodes.KEY_LEFT:
                    game.moveLeft();
                    break;
                case KeyCodes.KEY_RIGHT:
                    game.moveRight();
                    break;
                case KeyCodes.KEY_DOWN:
                    game.drop();
                    break;
                case KeyCodes.KEY_UP:
                    game.rotateCW();
                    break;
                case 32: // Space
                    game.drop();
                    break;
            }
            render();
            fireGameStateEvent(element, game.getScore(), game.isOver());
        } catch (Exception e) {
            GWT.log("Error handling key press: " + e.getMessage());
        }
    }

    private void startGameLoop() {
        gameTimer = new Timer() {
            @Override
            public void run() {
                if (!paused && !game.isOver()) {
                    boolean wasGameOver = game.step();
                    render();
                    fireGameStateEvent(element, game.getScore(), game.isOver());
                    if (wasGameOver) {
                        gameTimer.cancel();
                    }
                }
            }
        };
        gameTimer.scheduleRepeating(1000); // Move piece down every second
    }

    private void render() {
        // Clear canvas
        context.setFillStyle("#000");
        context.fillRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
//        context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());

        // Get current game state
        Grid state = game.getCurrentState();

        // Draw grid
        for (int x = 0; x < state.getWidth(); x++) {
            for (int y = 0; y < state.getHeight(); y++) {
                int cellValue = state.get(x, y);
                if (cellValue != 0) {
                    String color = getColorForValue(cellValue);
                    drawCell(x, y, color);
                }
            }
        }

        // Draw grid lines
        drawGridLines();
    }

    private void drawCell(int x, int y, String color) {
        context.setFillStyle(color);
        context.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw border
        context.setStrokeStyle("#fff");
        context.setLineWidth(1);
        context.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawGridLines() {
        context.setStrokeStyle("#333");
        context.setLineWidth(1);

        // Vertical lines
        for (int x = 0; x <= GAME_WIDTH; x++) {
            context.beginPath();
            context.moveTo(x * CELL_SIZE, 0);
            context.lineTo(x * CELL_SIZE, GAME_HEIGHT * CELL_SIZE);
            context.stroke();
        }

        // Horizontal lines
        for (int y = 0; y <= GAME_HEIGHT; y++) {
            context.beginPath();
            context.moveTo(0, y * CELL_SIZE);
            context.lineTo(GAME_WIDTH * CELL_SIZE, y * CELL_SIZE);
            context.stroke();
        }
    }

    private String getColorForValue(int value) {
        String[] colors = {"#000", "#0ff", "#00f", "#ffa500", "#ff0", "#0f0", "#800080", "#f00"};
        if (value >= 0 && value < colors.length) {
            return colors[value];
        }
        return "#fff"; // Default color
    }

    private native void fireGameStateEvent(Element element, int score, boolean gameOver) /*-{
        var name = gameOver ? "game-over-event" : name || "game-state-event";
        if ($wnd.parent && $wnd.parent.document) {
            var detail = {
                score: score,
                gameOver: gameOver
            };
            var event = new $wnd.parent.CustomEvent(name, {
                detail: detail,
                bubbles: true 
            });
            element.dispatchEvent(event);
        }
    }-*/;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
}