package labyrinth;

import javax.swing.Timer;
import java.io.IOException;


public class Player extends Character {
    private Timer animation;

    /**
     * Constructs a Player at the bottom left corner
     */
    public Player() throws IOException {
        super(40, 510, 40, 30, "data/player.png", false);
    }

    /**
     * Moves the player in a specified direction.
     * The distance moved is determined by the direction's x and y properties.
     */
    public void move(Direction d) {
        this.x += d.x * 40;
        this.y += d.y * 30;
    }
}
