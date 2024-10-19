package labyrinth;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class Game extends JPanel{
    
  private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 670;
    private final int FPS = 240;
    private final JPanel screen = this;
    private final Database database;
    private boolean paused = false;
    private Character pausedImage;
    private int levelNum = 0;
    private Level level;
    private Player player;
    private Dragon dragon;
    private Background background;
    private Timer enemyMovementTimer;
    private Timer newFrameTimer;
    private JLabel timeLabel;
    private long startTime;
    private Timer timer;
    private long stoppedTime;
    private long elapsedTime;
    private double elapsedTimeInSeconds;

    public Game() throws SQLException, IOException {
        this.addKeyListener(new PLayerMove());

        pausedImage = new Character(SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 125, 400, 250, "data/paused.png", false);

        restart(0);
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
        enemyMovementTimer = new Timer(800, new StartDragonWalkListener());
        enemyMovementTimer.start();
        database = new Database();
        startTimer();
    }

    /**
     * Starts a timer that updates every 100ms and displays it on the frame.
     */
    public void startTimer() {
        timeLabel = new JLabel(" ");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        startTime = System.currentTimeMillis();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = System.currentTimeMillis() - startTime;
                elapsedTimeInSeconds = (double) elapsedTime / 1000;
                timeLabel.setText(elapsedTimeInSeconds + " seconds");
            }
        });
        timer.start();
    }

    public void restartTimer() {
        startTime = System.currentTimeMillis();
        timer.restart();
    }

    public JLabel getTimer() {
        return timeLabel;
    }

    public Database getDatabase() {
        return database;
    }

    public void restart(int levelNum) {
        this.levelNum = levelNum;
        paused = false;
        try {
            level = new Level("data/level" + levelNum + ".txt");
            player = new Player();
            background = new Background();
            background.setCoords(player.getCoords());

            Random rand = new Random();
            do {
                int x = rand.nextInt(16) + 1; // Values from 1 to 16
                int y = rand.nextInt(16) + 1; // Values from 1 to 16
                dragon = new Dragon(x * 40, y * 30);
            } while (level.collides(dragon.getCoords()) || dragon.catches(player));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if the player reached the top right corner
     */
    public boolean isOver() {
        Point coords = player.getCoords();
        return coords.x == 18 && coords.y == 1;
    }

    /**
     * Toggles the game's paused state 
     */
    public void pause() {
        paused = !paused;
        if (paused) {
            timer.stop();
            stoppedTime = System.currentTimeMillis();
        } else {
            startTime += System.currentTimeMillis() - stoppedTime;
            timer.restart();
        }
    }

    /**
     * Draws all the characters on the frame.
     */
    @Override
    protected void paintComponent(Graphics g) {
        level.draw(g);
        player.draw(g);
        dragon.draw(g);
        background.draw(g);
        if (paused) pausedImage.draw(g);
    }

    /**
     * Used in a Timer object to update the dragon's location every 800ms
     */
    class StartDragonWalkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                if(level.collides(dragon.getCoords(), dragon.getDirection())){
                    do{
                        dragon.chooseDirection();
                    }while(level.collides(dragon.getCoords(),dragon.getDirection()));
                }
                dragon.move();
            }
        }
    }
    
    /**
     * Used in a Timer object to refresh the frame and listens to end of game conditions 240 times a second. 
     * If player was catched by a dragon, shows dialog that asks to play again 
     * If player completed 10 levels, it shows congrats dialog and asks to play again 
     */
    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
            if(dragon.catches(player)){
                timer.stop();
                String name = JOptionPane.showInputDialog(screen, "Enter your name: ", "The dragon caught you.", JOptionPane.INFORMATION_MESSAGE);
                if(name != null) database.putHighScore(name, levelNum, elapsedTimeInSeconds);
                int option = JOptionPane.showConfirmDialog(screen, "Want to try agian?", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    restart(0);
                    restartTimer();
                }
                else System.exit(0);
            }
            else if(isOver()){
                levelNum += 1;
                if(levelNum > 9){ //did all 10 levels 
                    timer.stop();
                    String name = JOptionPane.showInputDialog(screen, "You escaped in " + elapsedTimeInSeconds + " seconds! Enter your name: ", "You won!", JOptionPane.INFORMATION_MESSAGE);
                    if(name != null) database.putHighScore(name, levelNum, elapsedTimeInSeconds);
                    int option = JOptionPane.showConfirmDialog(screen, "Play again?" , "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        restart(0);
                        startTime = System.currentTimeMillis();
                        timer.restart();
                    }
                    else System.exit(0);
                }
                else restart(levelNum);
            }
            repaint();
        }
    }
    
    //player moving - using WASD keys
    class PLayerMove extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent key) {
        int kc = key.getKeyCode();
        Point coords = player.getCoords();
        switch (kc){
            case KeyEvent.VK_A: { // Changed from VK_LEFT to VK_A
                if(paused) return;
                if (!level.collides(coords, Direction.WEST)) {
                    player.move(Direction.WEST);
                }
                break;
            }
            case KeyEvent.VK_D: { // Changed from VK_RIGHT to VK_D
                if(paused) return;
                if (!level.collides(coords, Direction.EAST)) {
                    player.move(Direction.EAST);
                }
                break;
            }
            case KeyEvent.VK_W: { // Changed from VK_UP to VK_W
                if(paused) return;
                if (!level.collides(coords, Direction.NORTH)) {
                    player.move(Direction.NORTH);
                }
                break;
            }
            case KeyEvent.VK_S: { // Changed from VK_DOWN to VK_S
                if(paused) return;
                if (!level.collides(coords, Direction.SOUTH)) {
                    player.move(Direction.SOUTH);
                }
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                pause();
                break;
            }
        }
        
        background.setCoords(player.getCoords());      
    }
}

}
    

