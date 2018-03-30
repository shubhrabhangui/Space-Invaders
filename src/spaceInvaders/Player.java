package spaceInvaders;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Models implements Commons {

    private final int playerStartY = 540;
    private final int playerStartX = 300;

    ImageIcon playerIcon = new ImageIcon("src/pship.png");
    private int width;
    
    private static final Player player = new Player();
    
    
    public static Player getInstance() {
    		return player;
    }
    
    private Player() {
        setPlayer();
    }

    private void setPlayer() {
        width = playerIcon.getImage().getWidth(null);
        setIcon(playerIcon.getImage());
        setX(playerStartX);
        setY(playerStartY);
    }

    public void move() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= gameFrameWidth - 2 * width) {
            x = gameFrameWidth - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            dx = -2;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}