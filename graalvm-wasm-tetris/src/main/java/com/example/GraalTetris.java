package com.example;

import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSObject;
import org.graalvm.webimage.api.JSString;
import org.parttio.example.tetrislib.Game;
import org.parttio.example.tetrislib.Grid;
import org.parttio.example.tetrislib.Tetromino;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class GraalTetris {
    private static final int PAUSE_TIME_MS = 500;
    // Playfield width in tiles
    private static final int PLAYFIELD_W = 10;
    // Playfield height in tiles
    private static final int PLAYFIELD_H = 20;
    // Tile size in pixels
    private static final int tileSize = 30;
    private Game game;
    private CanvasRenderingContext2D context;
    private int score = -1;
    private int intervalId;
    private JSObject wrapper;

    public static void main(String[] args) {
        exportTetris(parentElement -> new GraalTetris().runTetris(parentElement));
    }

    @JS.Coerce
    @JS("console.log(s);")
    static native void consoleLog(String s);

    private void runTetris(JSObject parentElement) {
        this.wrapper = parentElement;
        JSObject left = createElement("button");
        left.set("innerText", JSString.of("←"));
        addEventListener(left, "click", e -> {
            game.moveLeft();
            drawGameState();
        });

        JSObject right = createElement("button");
        right.set("innerText", JSString.of("→"));
        addEventListener(right, "click", e -> {
            game.moveRight();
            drawGameState();
        });

        JSObject rotateCW = createElement("button");
        rotateCW.set("innerText", JSString.of("↻"));
        addEventListener(rotateCW, "click", e -> {
            game.rotateCW();
            drawGameState();
        });

        JSObject drop = createElement("button");
        drop.set("innerText", JSString.of("↓"));
        addEventListener(drop, "click", e -> {
            game.drop();
            drawGameState();
        });

        JSObject controls = createElement("div");

        appendChild(controls, rotateCW);
        appendChild(controls, right);
        appendChild(controls, drop);
        appendChild(controls, left);
        appendChild(parentElement, controls);

        // Keyboard controls
        addEventListener(document(), "keydown", e -> {
            int keyCode = getKeyCode(e);
            switch (keyCode) {
                case 37: // Left arrow
                    game.moveLeft();
                    drawGameState();
                    break;
                case 39: // Right arrow
                    game.moveRight();
                    drawGameState();
                    break;
                case 38: // Up arrow
                    game.rotateCW();
                    drawGameState();
                    break;
                case 32: // Down arrow
                    game.drop();
                    drawGameState();
                    break;
            }
        });

        JSObject canvas = createElement("canvas");
        appendChild(parentElement, canvas);
        setAttribute(canvas, "width", tileSize * PLAYFIELD_W);
        setAttribute(canvas, "height", tileSize * PLAYFIELD_H);

        context = get2dContext(canvas);

        game = new Game(10, 20);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        this.intervalId = setInterval(() -> {
            game.step();
            drawGameState();
        }, PAUSE_TIME_MS);

    }

    private void drawGameState() {
        int newScore = game.getScore();
        if(score != newScore) {
            fireGameStateEvent(wrapper, newScore, game.isOver());
        }
        score = newScore;
        if(game.isOver()) {
            // Stop the game loop
            clearInterval(intervalId);
        }

        // Reset and clear canvas
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

    @JS.Coerce
    @JS("clearInterval(intervalId);")
    private static native void clearInterval(int intervalId);

    @JS.Coerce
    @JS("window.graalTetris = r;")
    private static native void exportTetris(Consumer<JSObject> r);

    @JS.Coerce
    @JS("return setInterval(r, ms);")
    private static native int setInterval(Runnable r, int ms);

    private static void runWithUncaughtHandler(Runnable r) {
        try {
            r.run();
        } catch (Throwable e) {
            System.err.println("Uncaught exception in event listener");
            e.printStackTrace();
        }
    }


    @JS.Coerce
    @JS("element.dispatchEvent(new CustomEvent((gameOver ? 'game-over-event' : 'game-state-event'), {bubbles:true , detail:{'score': scrore, 'gameOver': gameOver}}));")
    static native void fireGameStateEvent(JSObject element, int scrore, boolean gameOver);

    @JS.Coerce
    @JS("o.addEventListener(event, (e) => handler(e));")
    static native void addEventListenerImpl(JSObject o, String event, EventHandler handler);

    static void addEventListener(JSObject o, String event, EventHandler handler) {
        addEventListenerImpl(o, event, e -> runWithUncaughtHandler(() -> handler.handleEvent(e)));
    }

    @JS.Coerce
    @JS("return canvas.getContext('2d');")
    public static native JSObject get2dContextJso(JSObject canvas);

    @JS.Coerce
    @JS("return document;")
    public static native JSObject document();

    @JS.Coerce
    @JS("return evt.keyCode;")
    public static native int getKeyCode(JSObject evt);

    @JS.Coerce
    @JS("context.clearRect(x, y, w, h);")
    public static native void _clearRect(JSObject context, int x, int y, int w, int h);

    @JS.Coerce
    @JS("context.fillRect(x, y, w, h);")
    public static native void _fillRect(JSObject context, int x, int y, int w, int h);

    @JS.Coerce
    @JS("context.fillStyle = color;")
    public static native void _setFillStyle(JSObject context, String color);

    public static CanvasRenderingContext2D get2dContext(JSObject canvas) {
        JSObject context = get2dContextJso(canvas);

        return new CanvasRenderingContext2D() {
            @Override
            public void clearRect(int x, int y, int w, int h) {
                _clearRect(context, x, y, w, h);
            }

            @Override
            public void setFillStyle(String color) {
                _setFillStyle(context, color);
            }

            @Override
            public void fillRect(int x, int y, int w, int h) {
                _fillRect(context, x, y, w, h);
            }
        };
    }

    @JS.Coerce
    @JS("return document.querySelector(selector);")
    public static native JSObject querySelector(String selector);

    @JS.Coerce
    @JS("return document.createElement(tag);")
    public static native JSObject createElement(String tag);

    @JS.Coerce
    @JS("elem.setAttribute(attribute, value);")
    public static native void setAttribute(JSObject elem, String attribute, Object value);

    @JS.Coerce
    @JS("parent.appendChild(child);")
    public static native void appendChild(JSObject parent, JSObject child);

    // TODO @JS.Import ?? Couldn't make it work...
    interface CanvasRenderingContext2D {
        void clearRect(int x, int y, int w, int h);

        void setFillStyle(String color);

        void fillRect(int x, int y, int w, int h);
    }

    @FunctionalInterface
    interface EventHandler {
        void handleEvent(JSObject event);
    }

}