package labyrinth;

import java.awt.*;
import javax.imageio.*;
import java.awt.Image;
import java.io.*;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Character {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    protected boolean isWall;

    /**
     * Constructs a Character with specified location, size, image, and wall status.
     */
    public Character(int x, int y, int width, int height, Image image, boolean isWall) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.isWall = isWall;
    }
    
    //constructor with image path
    public Character(int x, int y, int width, int height, String imagePath, boolean isWall) throws IOException {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Load image using the imagePath
        InputStream is = getClass().getClassLoader().getResourceAsStream(imagePath);
        if (is == null) {
            throw new IOException("Cannot find resource: " + imagePath);
        }
        this.image = new ImageIcon(ImageIO.read(is)).getImage();
        
        this.isWall = isWall;
    }
    
    /**
     * Draws the character's image at its position on the given graphics context.
     */
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    /**
     * @return a Point object representing the character's grid coordinates.
     */
    public Point getCoords(){
        return new Point(x/40,y/30);
    }
}