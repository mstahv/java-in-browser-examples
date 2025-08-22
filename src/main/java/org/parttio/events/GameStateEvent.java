package org.parttio.events;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;

public interface GameStateEvent extends Event {
    @JSProperty
    GameStateDetail getDetail();

}
