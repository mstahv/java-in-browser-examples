package org.parttio.tetris;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.shared.Registration;

@Tag("tetris-component")
@JavaScript("https://cjrtnc.leaningtech.com/4.2/loader.js")
public class CheerpJTetrisComponent extends Component implements HasSize {
    // /app is a special instruct for cheerpj, loads then from //tetris.jar
    // copied there with maven-resources-plugin during from org.vaadin.example:plain-java-tetris
    String jarUrl = "/app/tetris.jar";

    public CheerpJTetrisComponent() {
        setWidth("320px");
        setHeight("740px");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs(
        """
              window.tetrisElement  = this;
              
              async function Java_org_parttio_tetris_swing_SwingTetris_fireInBrowser(lib, scoreInt, gameOver) {
                // fire data coming from CheerpJ process as a CustomEvent
                // listened in Vaadin Java component
                if(gameOver) {
                  const evt = new CustomEvent("game-over-event", { detail: {score : scoreInt} });
                  window.tetrisElement.dispatchEvent(evt);
                } else {
                  const evt = new CustomEvent("game-state-event", { detail: {score : scoreInt} });
                  window.tetrisElement.dispatchEvent(evt);
                }                
              }
        
              async function Java_org_parttio_tetris_swing_SwingTetris_nativeSetApplication(
                lib,
                myApplication
              ) {
                window.myApplication = myApplication;
                console.log("Java application instance set on JavaScript side.");
                return new Promise(() => {}); // Keeps the function from returning
              }
                      
              (async function () {
              
                if(!cj3Module) {
                    await cheerpjInit({
                      version: 17,
                      natives: {
                        Java_org_parttio_tetris_swing_SwingTetris_fireInBrowser,
                        Java_org_parttio_tetris_swing_SwingTetris_nativeSetApplication,
                      },
                    });
                }
                cheerpjCreateDisplay(window.tetrisElement.clientWidth, window.tetrisElement.clientHeight, window.tetrisElement);
                await cheerpjRunJar("%s");
              })();
        """.formatted(jarUrl));
    }


    public Registration addGameStateListener(ComponentEventListener<GameStateEvent> listener) {
        return addListener(GameStateEvent.class, listener);
    }

    public Registration addGameOverListener(ComponentEventListener<GameOverEvent> listener) {
        return addListener(GameOverEvent.class, listener);
    }

    @DomEvent("game-state-event")
    public static class GameStateEvent extends GameEvent {
        public GameStateEvent(CheerpJTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }

    @DomEvent("game-over-event")
    public static class GameOverEvent extends GameEvent {
        public GameOverEvent(CheerpJTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, true, score);
        }
    }


    public static abstract class GameEvent extends ComponentEvent<CheerpJTetrisComponent> {
        private final int score;

        public GameEvent(CheerpJTetrisComponent source, boolean fromClient, @EventData("event.detail.score") int score) {
            super(source, fromClient);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

}
