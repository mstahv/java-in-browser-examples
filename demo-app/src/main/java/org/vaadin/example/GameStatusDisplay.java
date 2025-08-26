package org.vaadin.example;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import org.vaadin.firitin.util.style.LumoProps;

/**
 * A reusable component to display game status information.
 * Used by both TetrisWithCheerpJ and TetrisWithTeaVM views.
 */
public class GameStatusDisplay extends Card {

    static int counter = 0;
    Div scoreDisplay = new Div();

    public GameStatusDisplay(String description) {
        addThemeVariants(CardVariant.LUMO_COVER_MEDIA);
        setMedia(new Image("photos/sunset" + (counter++%3 +1) + ".jpeg", "Sunset"));
        add(new H2("Game info and Status"));
        add(new Paragraph(description));
        add(new Paragraph("Started plaing at (server time): " + java.time.LocalTime.now()));
        add(scoreDisplay);
        scoreDisplay.getStyle().setColor(LumoProps.PRIMARY_COLOR.var());
        scoreDisplay.getStyle().setFontWeight("bold");
        scoreDisplay.getStyle().setFontSize("2em");

        getStyle().setMaxWidth("400px");
        getStyle().setPadding(LumoProps.SPACE_M.var());
    }

    public void updateScore(int score) {
        scoreDisplay.setText("Score: " + score);
    }

    public void showGameOver(int score) {
        scoreDisplay.setText("Score: " + score);
        add(new Paragraph("Game Over at " + java.time.LocalTime.now() + "! Final score: " + score));
    }
}
