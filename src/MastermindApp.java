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

    public static int guessNum = 0;

    private static int CANVAS_WIDTH = 500;
    private static int CANVAS_HEIGHT = 800;

    public static final int BALL_RADIUS = 50;
    private static CanvasWindow canvas = new CanvasWindow("MACSTERMIND",CANVAS_WIDTH,CANVAS_HEIGHT);
    private Button resetButton;

    public String currentClicked = null;

    static int codeLength;

    private Image backgroundImg;

    private MastermindGame currentGame;

    public ArrayList<String> guessArray = new ArrayList<String>();

    private WinScreen winScreen;
    private LoseScreen loseScreen;

    public MastermindApp(int codeLength) {

        currentGame = new MastermindGame(false, codeLength);
        
        
        // fills the guessArray with nulls //
        for(int i = 0; i < codeLength; i++) {
            guessArray.add(null);
        }
        
        // determines what canvas to use based on code length //
        if(codeLength == 4) {
            backgroundImg = new Image("Board.png");
            canvas.closeWindow();
            CANVAS_WIDTH = 500;
            CANVAS_HEIGHT = 800;
            canvas = new CanvasWindow("Macstermind", CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        if (codeLength == 6){
            backgroundImg = new Image("BoardSixCode.png");
            canvas.closeWindow();
            CANVAS_WIDTH = 650;
            CANVAS_HEIGHT = 800;
            canvas = new CanvasWindow("Macstermind", CANVAS_WIDTH, CANVAS_HEIGHT);
        }

        backgroundImg.setMaxWidth(CANVAS_WIDTH);
        backgroundImg.setMaxHeight(CANVAS_HEIGHT);
        canvas.add(backgroundImg);

        // creates row at bottom with clickable colors //
        double offset = -1 * BALL_RADIUS;
        for(String color : MastermindGame.colorList) {
            Ellipse ball = new Ellipse(100 + offset,700,BALL_RADIUS,BALL_RADIUS);
            ball.setFillColor(new Color(MastermindGame.colorMap.get(color)[0], MastermindGame.colorMap.get(color)[1], MastermindGame.colorMap.get(color)[2], MastermindGame.colorMap.get(color)[3]));
            canvas.add(ball);
            offset += BALL_RADIUS;
            canvas.onClick(event -> {
                if(ball.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    currentClicked = color;
                    mouseBall.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2], MastermindGame.colorMap.get(color)[3]));
                };

            });
        }


        // little numbers in the corner that you can click to switch code length //
        GraphicsText numberSix = new GraphicsText("6");
        numberSix.setPosition(CANVAS_WIDTH - 30, CANVAS_HEIGHT - 10);

        GraphicsText numberFour = new GraphicsText("4");
        numberFour.setPosition(CANVAS_WIDTH - 60, CANVAS_HEIGHT - 10);

        if(codeLength == 4) {
            numberSix.setFontSize(40);
            numberSix.setFillColor(Color.GRAY);

            numberFour.setFontSize(40);
            numberFour.setFillColor(Color.RED);

            canvas.add(numberFour);
            canvas.add(numberSix);
            canvas.onClick(event -> {
                if(numberSix.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    canvas.removeAll();
                    guessNum = 0;
                    MastermindApp newApp = new MastermindApp(6);
                }
            });
        }
        if(codeLength == 6) {
            numberSix.setFontSize(40);
            numberSix.setFillColor(Color.RED);

            numberFour.setFontSize(40);
            numberFour.setFillColor(Color.GRAY);

            canvas.add(numberFour);
            canvas.add(numberSix);
            canvas.onClick(event -> {
                if(numberFour.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    canvas.removeAll();
                    guessNum = 0;
                    MastermindApp newApp = new MastermindApp(4);
                }
            });
        }


        

        // 'start new game' button
        Button startNewGameButton = new Button("Start new game?");
        startNewGameButton.setPosition(100,100);
        startNewGameButton.onClick(() -> {
            canvas.removeAll();
            guessNum = 0;
            MastermindApp newApp = new MastermindApp(codeLength);
        });

        resetButton = new Button("check");

        if(codeLength == 4) {
            resetButton.setPosition(CANVAS_WIDTH * 0.775, CANVAS_HEIGHT * 0.75);
        }
        else {
            resetButton.setPosition((CANVAS_WIDTH * 0.775) + BALL_RADIUS - 14, CANVAS_HEIGHT * 0.75);
        }
        
        resetButton.setScale(BALL_RADIUS, BALL_RADIUS);
        resetButton.onClick(() -> {

            // this is a shotty algorithm tbh //
            // just supposed to remove the mouseball so it doesn't hang around //
            if(currentClicked != null) {
                mouseBall.setPosition(100000,100000);
            }
            currentClicked = null;
            if(!guessArray.contains(null)) {
                resetButton.setPosition(new Point(resetButton.getX(), resetButton.getY() - BALL_RADIUS));

                // tests // 
                // System.out.println("actual code: " + MastermindGame.printList(currentGame.secretCode));
                // System.out.println("guess array: " + MastermindGame.printList(guessArray));
                // System.out.println("guess pegs:  " + MastermindGame.printList(currentGame.checkSecretCode(guessArray)));

                populatePegs(currentGame.checkSecretCode(guessArray));

                // WHATEVER HAPPENS WHEN PLAYER WINS GAME //
                if(currentGame.checkSecretCode(guessArray).equals(new ArrayList<String>(Arrays.asList("red","red","red","red")))) {
                    canvas.remove(resetButton);
                    winScreen = new WinScreen(canvas);
                    canvas.add(startNewGameButton);

                    // adds code to the top of the screen //
                    double toTheRight = 0;
                    for(String codeColor : currentGame.getSecretCode()) {
                        System.out.println(codeColor);
                        Ellipse codeBall = new Ellipse(100 - BALL_RADIUS + toTheRight, 700 - (13 * BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
                        codeBall.setFillColor(new Color(MastermindGame.colorMap.get(codeColor)[0], MastermindGame.colorMap.get(codeColor)[1], MastermindGame.colorMap.get(codeColor)[2]));
                        canvas.add(codeBall);
                        toTheRight += BALL_RADIUS;
        }
                }
                guessNum += 1;

                // WHATEVER HAPPENS WHEN PLAYER LOSES GAME //
                if(guessNum == 10) {
                    canvas.remove(resetButton);
                    loseScreen = new LoseScreen(canvas);
                    canvas.add(startNewGameButton);
                    // adds code to the top of the screen //
                    double toTheRight = 0;
                    for(String codeColor : currentGame.getSecretCode()) {
                        System.out.println(codeColor);
                        Ellipse codeBall = new Ellipse(100 - BALL_RADIUS + toTheRight, 700 - (13 * BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
                        codeBall.setFillColor(new Color(MastermindGame.colorMap.get(codeColor)[0], MastermindGame.colorMap.get(codeColor)[1], MastermindGame.colorMap.get(codeColor)[2]));
                        canvas.add(codeBall);
                        toTheRight += BALL_RADIUS;
                    }
                }
                else {
                    int guessIndex = 0;
                    for(int n = 0; n < codeLength; n++) {
                        guessIndex += 1;
                        final int num = guessIndex - 1;
                        Rectangle emptyRect = new Rectangle(guessIndex * BALL_RADIUS, (CANVAS_HEIGHT / 2) + ((4 - guessNum) * BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
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
                        guessArray = MastermindGame.resetGuess(guessArray);
                    }
                }
            }
        });

        // makes a little ball that follows the cursor with the selected color //
        mouseBall = new Ellipse(100000,100000, 20, 20);
        if(currentClicked == null) {
            mouseBall.setFillColor(new Color(0,0,0));
        }
        canvas.add(mouseBall);

    
        int guessIndex = 0;
        for(int n = 0; n < codeLength; n++) {
            guessIndex += 1;
            final int num = guessIndex - 1;
            Rectangle emptyRect = new Rectangle(guessIndex * BALL_RADIUS, (CANVAS_HEIGHT / 2) + (4 * BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
            canvas.onClick(event -> {
                if(emptyRect.testHit(event.getPosition().getX(),event.getPosition().getY())){
                    Ellipse guessEllipse = new Ellipse(emptyRect.getX(),emptyRect.getY(), BALL_RADIUS, BALL_RADIUS);
                    if(currentClicked != null) {
                        guessEllipse.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2], MastermindGame.colorMap.get(currentClicked)[3]));
                        canvas.add(guessEllipse);
                        canvas.add(mouseBall);
                        guessArray.set(num, currentClicked);
                    }
                };
            });
            canvas.add(emptyRect);
        }        

        canvas.onMouseMove(event -> {
            if(currentClicked != null) {
                canvas.remove(mouseBall);
                mouseBall.setCenter(event.getPosition().getX(),event.getPosition().getY());
                mouseBall.setFillColor(new Color(MastermindGame.colorMap.get(currentClicked)[0], MastermindGame.colorMap.get(currentClicked)[1], MastermindGame.colorMap.get(currentClicked)[2], MastermindGame.colorMap.get(currentClicked)[3]/2));
            }
            canvas.add(mouseBall);
        });

        canvas.add(resetButton);
        }

    

    public void populatePegs(ArrayList<String> pegList) {
        double xOffset = 0;
        double yOffset = (4.25 - guessNum) * BALL_RADIUS;

        if(pegList.size() == 4) {
            for(String peg : pegList) {
                if(peg.equals("red")) {
                    Ellipse redPeg = new Ellipse((CANVAS_WIDTH * 0.6) + (xOffset), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    redPeg.setFillColor(new Color(255,0,0));
                    canvas.add(redPeg);
                }
                if(peg.equals("white")) {
                    Ellipse whitePeg = new Ellipse((CANVAS_WIDTH * 0.6) + (xOffset), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    whitePeg.setFillColor(Color.WHITE);
                    canvas.add(whitePeg);
                }
                if(peg.equals("blank")) {
                    Ellipse grayPeg = new Ellipse((CANVAS_WIDTH * 0.6) + (xOffset), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    grayPeg.setFillColor(Color.GRAY);
                    canvas.add(grayPeg);
                }
                xOffset += BALL_RADIUS / 2;
            }
        }
        else {
            for(String peg : pegList) {
                if(peg.equals("red")) {
                    Ellipse redPeg = new Ellipse((CANVAS_WIDTH * 0.5) + (xOffset) + (BALL_RADIUS * 1.5), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    redPeg.setFillColor(new Color(255,0,0));
                    canvas.add(redPeg);
                }
                if(peg.equals("white")) {
                    Ellipse whitePeg = new Ellipse((CANVAS_WIDTH * 0.5) + (xOffset) + (BALL_RADIUS * 1.5), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    whitePeg.setFillColor(Color.WHITE);
                    canvas.add(whitePeg);
                }
                if(peg.equals("blank")) {
                    Ellipse grayPeg = new Ellipse((CANVAS_WIDTH * 0.5) + (xOffset) + (BALL_RADIUS * 1.5), (CANVAS_HEIGHT / 2) + yOffset, (BALL_RADIUS / 2), BALL_RADIUS / 2);
                    grayPeg.setFillColor(Color.GRAY);
                    canvas.add(grayPeg);
                }
                xOffset += BALL_RADIUS / 2;
            }
        }
        
    }

    public static void main(String[] args) {
        
        // TESTING ENVIRONMENT
        
        // MastermindGame game = new MastermindGame(4);
        // System.out.println(game);
        // ArrayList<String> guessArray = new ArrayList<String>(Arrays.asList("blue","blue","blue","blue"));
        // System.out.println("Secretguess:  " + MastermindGame.printList(guessArray));
        // System.out.println("\n\n\n\n\n");
        // System.out.println(MastermindGame.printList(game.checkSecretCode(guessArray)));
    
        MastermindApp newApp = new MastermindApp(4);
        
    }

}