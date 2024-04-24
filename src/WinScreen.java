import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Ellipse;
import java.lang.Math;
import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;


public class WinScreen extends GraphicsGroup {
    static int SPIN_RADIUS = 200;
    private CanvasWindow canvas;    
    private GraphicsGroup ballGroup;
    private ArrayList<Ellipse> balls = new ArrayList<>();
    private GraphicsText winText;

    private double time = 0;

    public WinScreen (CanvasWindow canvas){
        this.canvas = canvas;
        ballGroup = new GraphicsGroup();
        winText = new GraphicsText("You Win!");
        winText.setCenter(canvas.getCenter());
        this.add(winText);

        for (String color : MastermindGame.colorList) {
            balls.add(new Ellipse(SPIN_RADIUS * Math.sin(Math.PI/4 * balls.size()), SPIN_RADIUS * Math.cos(Math.PI/4 * balls.size()), MastermindApp.BALL_RADIUS, MastermindApp.BALL_RADIUS));
            balls.get(balls.size() - 1).setFillColor(new Color(
                MastermindGame.colorMap.get(color)[0], 
                MastermindGame.colorMap.get(color)[1], 
                MastermindGame.colorMap.get(color)[2],
                MastermindGame.colorMap.get(color)[3]));
            ballGroup.add(balls.get(balls.size() - 1));
        }
        ballGroup.setCenter(canvas.getCenter());

        this.add(ballGroup);

        canvas.add(this);
        canvas.animate(a -> {
            winText.setY(canvas.getCenter().getY() + Math.sin(time * 0.02) * 70);
            ballGroup.rotateBy(0.3);
            time ++;
        });
    }

}
