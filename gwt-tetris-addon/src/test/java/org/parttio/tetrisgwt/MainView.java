package org.parttio.tetrisgwt;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        H1 title = new H1("GWT Tetris Demo");
        Paragraph instructions = new Paragraph(
            "Use arrow keys to move pieces. Up arrow to rotate, down arrow to drop."
        );
        
        GWTTetrisComponent tetris = new GWTTetrisComponent();
        Paragraph scoreDisplay = new Paragraph("Score: 0");
        
        tetris.addGameStateListener(e -> {
            scoreDisplay.setText("Score: " + e.getScore());
        });
        
        tetris.addGameOverListener(e -> {
            scoreDisplay.setText("Game Over! Final Score: " + e.getScore());
        });
        
        add(title, instructions, tetris, scoreDisplay);
    }
}