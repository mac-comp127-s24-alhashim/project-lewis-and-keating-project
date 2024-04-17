import java.util.*;

import java.awt.Color;


import edu.macalester.graphics.*;

import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;


class MastermindApp {

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 800;

    public static final int BALL_RADIUS = 50;
    private final CanvasWindow canvas;
    private Button resetButton;

    public String currentClicked = null;

    private Image backgroundImg = new Image("Board.png");

    private MastermindGame currentGame;

    public MastermindApp() {
        canvas = new CanvasWindow("MACSTERMIND",CANVAS_WIDTH,CANVAS_HEIGHT);
        backgroundImg.setMaxWidth(CANVAS_WIDTH);
        backgroundImg.setMaxHeight(CANVAS_HEIGHT);
        canvas.add(backgroundImg);
        


        resetButton = new Button("DO A THING");
        resetButton.setPosition(CANVAS_WIDTH * 0.1, CANVAS_HEIGHT * 0.5);
        resetButton.onClick(() -> {
            currentGame = new MastermindGame(false, 6);
            resetButton.setPosition(new Point(resetButton.getX(), resetButton.getY() + 50));
            GraphicsText label = new GraphicsText(MastermindGame.printList(currentGame.getSecretCode()));
            label.setPosition(CANVAS_WIDTH / 3, CANVAS_HEIGHT / 3);
            canvas.add(label);

        });

        int offset = 0;
        for(String color : MastermindGame.colorList) {
            Ellipse ball = new Ellipse(100 + offset,700,BALL_RADIUS,BALL_RADIUS);
            ball.setFillColor(new Color(MastermindGame.colorMap.get(color)[0], MastermindGame.colorMap.get(color)[1], MastermindGame.colorMap.get(color)[2]));
            // canvas.add(ball);
            offset += BALL_RADIUS;
            canvas.onClick(event -> {
                if(ball.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    currentClicked = color;
                };
            });
        }

        canvas.add(resetButton);


        
    }

    public static void main(String[] args) {
        
        // tests
        
        MastermindGame game = new MastermindGame(true, 4);
        System.out.println(game);
    
        ArrayList<String> guessArray = new ArrayList<String>(Arrays.asList("blue","blue","blue","white"));
        System.out.println("Secretguess:  " + MastermindGame.printList(guessArray));
        System.out.println("\n\n\n\n\n");
        System.out.println(MastermindGame.printList(game.checkSecretCode(guessArray)));
    
        MastermindApp newApp = new MastermindApp();
        
    }

}