import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.ui.Button;


class MastermindApp {

    private Ellipse mouseBall;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 800;

    public static final int BALL_RADIUS = 50;
    private final CanvasWindow canvas;
    private Button resetButton;

    public String currentClicked = null;

    static int codeLength;

    private Image backgroundImg;

    private MastermindGame currentGame;

    public ArrayList<String> guessArray = new ArrayList<String>();

    public MastermindApp(int codeLength) {
        
        // fills the guessArray with nulls //
        for(int i = 0; i < codeLength; i++) {
            guessArray.add(null);
        }

        // determines what canvas to use based on code length //
        if(codeLength == 4) {
            backgroundImg = new Image("Board.png");
        }
        canvas = new CanvasWindow("MACSTERMIND",CANVAS_WIDTH,CANVAS_HEIGHT);
        backgroundImg.setMaxWidth(CANVAS_WIDTH);
        backgroundImg.setMaxHeight(CANVAS_HEIGHT);
        canvas.add(backgroundImg);


        // TODO: find what is causing error after you click the button like three times for some reason //
        resetButton = new Button("check guess");
        resetButton.setPosition(CANVAS_WIDTH * 0.75, CANVAS_HEIGHT * 0.75);
        resetButton.onClick(() -> {


            // this is a shotty algorithm tbh //
            // just supposed to remove the mouseball so it doesn't hang around //
            if(currentClicked != null) {
                canvas.remove(mouseBall);
            }
            currentClicked = null;
            
            currentGame = new MastermindGame(false, codeLength);
            resetButton.setPosition(new Point(resetButton.getX(), resetButton.getY() - BALL_RADIUS));
            
            GraphicsText checkPegs = new GraphicsText();
            canvas.add(checkPegs);

            if(!guessArray.contains(null)) {
                canvas.remove(checkPegs);
                checkPegs = new GraphicsText(MastermindGame.printList((currentGame.checkSecretCode(guessArray))));
            }

            checkPegs.setPosition(CANVAS_WIDTH / 4, CANVAS_HEIGHT / 4);
            canvas.add(checkPegs);

        });

        // makes a little ball that follows the cursor with the selected color //
        mouseBall = new Ellipse(100000,100000, 10, 10);
        if(currentClicked == null) {
            mouseBall.setFillColor(new Color(0,0,0));
        }

        canvas.add(mouseBall);

        int offset = 0;
        for(String color : MastermindGame.colorList) {
            Ellipse ball = new Ellipse(100 + offset,700,BALL_RADIUS,BALL_RADIUS);
            ball.setFillColor(new Color(MastermindGame.colorMap.get(color)[0], MastermindGame.colorMap.get(color)[1], MastermindGame.colorMap.get(color)[2]));
            canvas.add(ball);
            offset += BALL_RADIUS;
            canvas.onClick(event -> {
                if(ball.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    currentClicked = color;
                    mouseBall.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2]));
                };
            });
        }

        int guessIndex = 0;
        for(int n = 0; n < codeLength; n++) {
            guessIndex += 1;
            final int num = guessIndex - 1;
            Rectangle emptyRect = new Rectangle(guessIndex * BALL_RADIUS, (CANVAS_HEIGHT / 2) + (4 * BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
            canvas.onClick(event -> {
                if(emptyRect.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    Ellipse guessEllipse = new Ellipse(emptyRect.getX(),emptyRect.getY(), BALL_RADIUS, BALL_RADIUS);
                    if(currentClicked != null) {
                        guessEllipse.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2]));
                        canvas.add(guessEllipse);
                        guessArray.set(num, currentClicked);
                    }
                };
            });
            canvas.add(emptyRect);
        }


        canvas.onMouseMove(event -> {
            canvas.remove(mouseBall);
            if(currentClicked != null) {
                mouseBall.setCenter(event.getPosition().getX(),event.getPosition().getY());
                mouseBall.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2]));
            }
            canvas.add(mouseBall);
        });

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
    
        MastermindApp newApp = new MastermindApp(4);
        
    }

}