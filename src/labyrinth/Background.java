package labyrinth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Background {
    private final int SCREEN_WIDTH = 775;
    private final int SCREEN_HEIGHT = 615;

    /**
     * Coordinates of the upper left corner of the circle
     */
    private int x;
    private int y;
    private Area black;
    private Ellipse2D.Double focus;
   
    public void setCoords(Point p){
        this.x = (p.x - 3)*40+10;
        this.y = (p.y - 3)*30;
    }
    
    /**
     * Calculates the area of circle on the player with a radius of 3 and fills background with black.
     * @param g Graphics object 
     */
    public void draw(Graphics g){  
    Graphics2D g2d = (Graphics2D) g;
    black = new Area(new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));
    g2d.setColor(new Color(0,0,0,120));
    g2d.fill(black);
    // Calculate the diameter for the circle, which is the smaller of the two divided by 3
    int diameter = Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) / 3;

    int circleX = x + (SCREEN_WIDTH/3 - diameter) / 2;
    int circleY = y + (SCREEN_HEIGHT/3 - diameter) / 2;
    focus = new Ellipse2D.Double(circleX, circleY, diameter, diameter);
    black.subtract(new Area(focus)); // remove the circle from the original area
    g2d.setColor(Color.BLACK);
    g2d.fill(black);
}
    
}
