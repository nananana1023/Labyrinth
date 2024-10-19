package labyrinth;

import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;


public class Level {
    private final int TILE_WIDTH = 40;
    private final int TILE_HEIGHT = 30;
    Character[][] grid;
    
    public Level(String path) throws IOException {
        loadLevel(path);
    }
    
    /**
     * Generates a grid from a text file containing zeros and empty spaces where zero indicates a wall and empty space a floor.
     */
    public void loadLevel(String path) throws FileNotFoundException, IOException {   
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        grid = new Character[19][19];
        int y = 0;
        String line;
        while ((line = br.readLine()) != null) {
            int x = 0;
            for (char spriteType : line.toCharArray()) {                   
                if (spriteType == '0') {
                   InputStream is1 = getClass().getClassLoader().getResourceAsStream("data/wall.jpg");
                   if (is == null) {
                       throw new FileNotFoundException("Cannot find resource: data/wall.jpg");
                   }
                   Image image = new ImageIcon(ImageIO.read(is1)).getImage();
                   grid[y][x] = new Character(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, image, true);
               } else {
                   InputStream is2 = getClass().getClassLoader().getResourceAsStream("data/floor.jpg");
                   if (is == null) {
                       throw new FileNotFoundException("Cannot find resource: data/floor.jpg");
                   }
                   Image image = new ImageIcon(ImageIO.read(is2)).getImage();
                   grid[y][x] = new Character(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, image, false);
               }
                       x++;
            }
            y++;
        }
    }

    /**
     * Draws the grid 
     */
    public void draw(Graphics g) {
        for (Character[] row : grid){
            for(Character c : row){
                c.draw(g);
            }
        }
    }
    
    /**
     * Checks if the given direction next to the given point is a wall.
     */
    public boolean collides(Point coords, Direction d) {
        if(coords.x + d.x > 18) return true;
        Character object = grid[coords.y + d.y][coords.x + d.x];
        return object.isWall;
    }
    
    /**
     * Checks if the given point is a wall.
     */
    public boolean collides(Point coords) {
        Character object = grid[coords.y][coords.x];
        return object.isWall;
    }
   
}
