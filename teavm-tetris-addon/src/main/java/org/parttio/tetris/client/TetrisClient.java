package org.parttio.tetris.client;

import org.parttio.example.tetrislib.Game;
import org.parttio.example.tetrislib.Grid;
import org.parttio.example.tetrislib.Tetromino;
import org.parttio.tetris.client.events.GameOverEvent;
import org.parttio.tetris.client.events.GameStateEvent;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class TetrisClient {

    private static final int PAUSE_TIME_MS = 500;

    // Playfield width in tiles
    private static final int PLAYFIELD_W = 10;

    // Playfield height in tiles
    private static final int PLAYFIELD_H = 20;

    // Tile size in pixels
    private static final int tileSize = 30;

    private final HTMLCanvasElement canvas;
    private final Game game;
    private int intervalId;
    private HTMLElement wrapper;
    private int oldScore = 0;

    @JSExport
    public TetrisClient() {
        game = new Game(10, 20);
        var document = HTMLDocument.current();
        wrapper = document.createElement("div");
        canvas = (HTMLCanvasElement) document.createElement("canvas");
        canvas.setWidth(tileSize * PLAYFIELD_W);
        canvas.setHeight(tileSize * PLAYFIELD_H);
        canvas.setHeight(600);

        HTMLElement left = document.createElement("button").withText("⬅");
        left.addEventListener("click", e -> {
            game.moveLeft();
            drawGameState();
        });
        HTMLElement right = document.createElement("button").withText("➡");
        right.addEventListener("click", e -> {
            game.moveRight();
            drawGameState();
        });
        HTMLElement rotateCW = document.createElement("button").withText("↻");
        rotateCW.addEventListener("click", e -> {
            game.rotateCW();
            drawGameState();
        });
        HTMLElement rotateCCW = document.createElement("button").withText("↺");
        rotateCCW.addEventListener("click", e -> {
            game.rotateCCW();
            drawGameState();
        });

        HTMLElement drop = document.createElement("button").withText("⬇");
        drop.addEventListener("click", e -> {
            game.drop();
            drawGameState();
        });
        document.addEventListener("keydown", e -> {
            KeyboardEvent ke = (KeyboardEvent) e;
            switch (ke.getKeyCode()) {
                case 37: // Left arrow
                    left.click();
                    break;
                case 39: // Right arrow
                    right.click();
                    break;
                case 38: // Up arrow
                    rotateCW.click();
                    break;
                case 40: // Down arrow
                    rotateCCW.click();
                    break;
                case 32: // Space bar
                    drop.click();
                    break;
            }
            drawGameState();
        });

        HTMLElement controls = document.createElement("div");
        left.setClassName("control-left");
        right.setClassName("control-right");
        rotateCW.setClassName("control-rotate-cw");
        rotateCCW.setClassName("control-rotate-ccw");
        drop.setClassName("control-drop");
        controls.appendChild(left);
        controls.appendChild(right);
        controls.appendChild(rotateCW);
        controls.appendChild(rotateCCW);
        controls.appendChild(drop);
        wrapper.appendChild(controls);
        wrapper.appendChild(canvas);

        drawGameState();

        this.intervalId = Window.setInterval(() -> {
            game.step();
            drawGameState();
            boolean over = game.isOver();
            int score = game.getScore();
            if(over) {
                Window.clearInterval(intervalId);
                // send a custom event to notify the game is over
                GameOverEvent gameOverEvent = createGameOverEvent(score);
                HTMLElement parentNode = (HTMLElement) wrapper.getParentNode();
                parentNode.dispatchEvent(gameOverEvent);
            } else if (score != oldScore) {
                // send a custom event to notify the game state
                GameStateEvent gameStateEvent = createGameStateEvent(score);
                HTMLElement parentNode = (HTMLElement) wrapper.getParentNode();
                parentNode.dispatchEvent(gameStateEvent);
                oldScore = score;
            }

        }, PAUSE_TIME_MS);

    }

    @JSExport
    @JSProperty
    public HTMLElement getWrapper() {
        return wrapper;
    }

    @JSExport
    public static TetrisClient tetris() {
        return new TetrisClient();
    }

    /**
     * Draw the current game state.
     *
     */
    protected synchronized void drawGameState() {
        // Reset and clear canvas
        CanvasRenderingContext2D context = (CanvasRenderingContext2D) canvas.getContext("2d");
        context.clearRect(0, 0, tileSize * PLAYFIELD_W, tileSize * PLAYFIELD_H);
        context.setFillStyle("black");

        context.fillRect(0, 0, game.getWidth() * tileSize + 2, game.getHeight()
                * tileSize + 2);

        // Draw the tetrominoes
        Grid state = game.getCurrentState();
        for (int x = 0; x < state.getWidth(); x++) {
            for (int y = 0; y < state.getHeight(); y++) {
                int tile = state.get(x, y);
                if (tile > 0) {
                    String color = Tetromino.get(tile).getColor();
                    context.setFillStyle(color);
                    context.fillRect(x * tileSize + 1, y * tileSize + 1,
                            tileSize - 2, tileSize - 2);
                }

            }
        }

    }

    @JSBody(
            params = { "score" },
            script = "return new CustomEvent('game-over-event', {bubbles:true, detail:{'score': score}});"
    )
    private static native GameOverEvent createGameOverEvent(int score);

    @JSBody(
            params = { "score" },
            script = "return new CustomEvent('game-state-event', {bubbles:true , detail:{'score': score}});"
    )
    private static native GameStateEvent createGameStateEvent(int score);

}
