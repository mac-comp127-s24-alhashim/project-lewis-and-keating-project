import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.Ellipse;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;


public class LoseScreen extends GraphicsGroup {
    static int SPIN_RADIUS = 200;
    private CanvasWindow canvas;    
    private GraphicsGroup ballGroup;
    private ArrayList<Ellipse> balls = new ArrayList<>();
    private GraphicsText winText;
    private Rectangle screenTint;

    private double time = 0;

    public LoseScreen (CanvasWindow canvas){
        Random rand = new Random();
        this.canvas = canvas;
        screenTint = new Rectangle(new Point(0,0), new Point(canvas.getWidth(), canvas.getHeight()));
        screenTint.setFillColor(new Color(255,255,255,90));
        this.add(screenTint);
        ballGroup = new GraphicsGroup();
        winText = new GraphicsText("You Lose!");
        winText.setFontSize(100);
        winText.setCenter(canvas.getCenter());
        this.add(winText);

        for (String color : MastermindGame.colorList) {
            balls.add(new Ellipse(rand.nextInt(canvas.getWidth()), -100 * balls.size(), MastermindApp.BALL_RADIUS, MastermindApp.BALL_RADIUS));
            balls.get(balls.size() - 1).setFillColor(new Color(
                MastermindGame.colorMap.get(color)[0], 
                MastermindGame.colorMap.get(color)[1], 
                MastermindGame.colorMap.get(color)[2],
                MastermindGame.colorMap.get(color)[3]));
            ballGroup.add(balls.get(balls.size() - 1));
        }

        this.add(ballGroup);

        canvas.add(this);
        canvas.animate(a -> {
            for (Ellipse ball : balls) {
                if (ball.getY() < canvas.getHeight()) {
                    ball.setY(ball.getY() + 2);
                }
                else {
                    ball.setPosition(new Point(rand.nextInt(canvas.getWidth()), -MastermindApp.BALL_RADIUS));
                }
            }
            time ++;
        });
    }

}
