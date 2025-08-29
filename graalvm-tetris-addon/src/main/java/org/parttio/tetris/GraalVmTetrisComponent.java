package org.parttio.tetris;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.shared.Registration;

@Tag("graalvm-tetris-component")
@JavaScript("context://graaltetris.js")
public class GraalVmTetrisComponent extends Component {

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        getElement().executeJs("""
            const element = this;
            if(window.graalTetris) {
                window.graalTetris(element);
            } else {
               // wasm init not yet done, timer until loaded...
                const interval = setInterval(() => {
                    console.log("Checking for graalTetris by WASM...");
                     if(window.graalTetris) {
                          console.log("Found it 💪");
                          clearInterval(interval);
                          window.graalTetris(element);
                     }
                }, 50);
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
        public GameStateEvent(GraalVmTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    @DomEvent("game-over-event")
    public static class GameOverEvent extends GameEvent {
        public GameOverEvent(GraalVmTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }


    public static abstract class GameEvent extends ComponentEvent<GraalVmTetrisComponent> {
        private final int score;

        public GameEvent(GraalVmTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, fromClient);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }
}
