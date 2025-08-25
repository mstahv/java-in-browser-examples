package org.parttio.tetris;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

@Tag("tetris-component")
@JsModule("./tetris/tetrisConnector.js")
public class TetrisComponent extends Component {

    public TetrisComponent() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("""
                    const c = tetris();
                    this.appendChild(c.wrapper);
                
                    if(false) {
                        this.addEventListener("game-over-event", (e) => {
                            const gameOverEvent = e.detail;
                            alert("Game Over! Score: " + gameOverEvent.score);
                        });
                        this.addEventListener("game-state-event", (e) => {
                            const gameStateEvent = e.detail;
                            console.log("Game State Updated: " + gameStateEvent.score);
                        });
                    }
                
                """);

    }

    public Registration addGameStateListener(ComponentEventListener<GameStateEvent> listener) {
        return addListener(GameStateEvent.class, listener);
    }

    public Registration addGameOverListener(ComponentEventListener<GameOverEvent> listener) {
        return addListener(GameOverEvent.class, listener);
    }

    @DomEvent("game-state-event")
    public static class GameStateEvent extends GameEvent {
        public GameStateEvent(TetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    @DomEvent("game-over-event")
    public static class GameOverEvent extends GameEvent {
        public GameOverEvent(TetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }


    public static abstract class GameEvent extends ComponentEvent<TetrisComponent> {
        private final int score;

        public GameEvent(TetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, fromClient);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

}
