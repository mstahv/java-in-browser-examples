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
public class TeaVMTetrisComponent extends Component {

    public TeaVMTetrisComponent() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("""
                    const c = teavmtetris();
                    this.appendChild(c.wrapper);
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
        public GameStateEvent(TeaVMTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    @DomEvent("game-over-event")
    public static class GameOverEvent extends GameEvent {
        public GameOverEvent(TeaVMTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }


    public static abstract class GameEvent extends ComponentEvent<TeaVMTetrisComponent> {
        private final int score;

        public GameEvent(TeaVMTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, fromClient);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

}
