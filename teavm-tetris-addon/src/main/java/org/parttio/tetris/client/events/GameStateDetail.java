package org.parttio.tetris.client.events;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface GameStateDetail extends JSObject {
    @JSProperty
    int getScore();

    @JSProperty
    void setScore(int score);
}
