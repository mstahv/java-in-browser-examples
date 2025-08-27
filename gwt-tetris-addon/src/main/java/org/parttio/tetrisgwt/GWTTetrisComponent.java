package org.parttio.tetrisgwt;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.shared.Registration;

@Tag("tetris-gwt-component")
@JavaScript("context://tetris/tetris.nocache.js")
public class GWTTetrisComponent extends Component {

    public GWTTetrisComponent() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("""
                const element = this;
                if(window.startGwtTetris) {
                    window.startGwtTetris(element);
                } else {
                   window.tetrisElement = element;
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
        public GameStateEvent(GWTTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    @DomEvent("game-over-event")
    public static class GameOverEvent extends GameEvent {
        public GameOverEvent(GWTTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    public static abstract class GameEvent extends ComponentEvent<GWTTetrisComponent> {
        private final int score;

        public GameEvent(GWTTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, fromClient);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

}