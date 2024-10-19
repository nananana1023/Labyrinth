package labyrinth;

import javax.swing.Timer;
import java.io.*;

public class Dragon extends Character {
    private Direction direction;

   /**
     * Constructs a Dragon at the specified coordinates.
     * Initializes the dragon with an image, sets its initial direction, and starts its animation.
     */
    public Dragon(int x, int y) throws IOException{
        super(x, y, 40, 30, "data/dragon.png", false);
        chooseDirection();
    }
    
    /**
     * Moves the dragon in its current direction.
     */
    public void move(){
        this.x += direction.x*40;
        this.y += direction.y*30;
    }
    
     /**
     * Randomly chooses a new direction for the dragon to move.
     */
    public void chooseDirection(){
        Direction direction = Direction.SOUTH;
        int randomDirection = (int) Math.round(Math.random()*3) + 1;
        switch(randomDirection){
            case 1: direction = Direction.SOUTH; break;
            case 2: direction = Direction.EAST; break;
            case 3: direction = Direction.NORTH; break;
            case 4: direction = Direction.WEST;
        }
        this.direction = direction;
    }
    
     /**
     * Checks if the dragon catches the player by comparing their coordinates.
     * 
     * @param player the player character to check against.
     * @return true if the dragon is on the same position as the player, otherwise false.
     */
    public boolean catches(Player player){
        Point heroCoords = player.getCoords();
        Point enemyCoords = this.getCoords();
        
        if(enemyCoords.equals(heroCoords) ||
           enemyCoords.addDirection(Direction.SOUTH).equals(heroCoords) ||
           enemyCoords.addDirection(Direction.SW).equals(heroCoords)    ||
           enemyCoords.addDirection(Direction.WEST).equals(heroCoords)  ||
           enemyCoords.addDirection(Direction.NW).equals(heroCoords)    ||
           enemyCoords.addDirection(Direction.NORTH).equals(heroCoords) ||
           enemyCoords.addDirection(Direction.NE).equals(heroCoords)    ||
           enemyCoords.addDirection(Direction.EAST).equals(heroCoords)  ||
           enemyCoords.addDirection(Direction.SE).equals(heroCoords) ) 
             return true;
        else return false;
    }
    
    public Direction getDirection(){
        return direction;
    }
}