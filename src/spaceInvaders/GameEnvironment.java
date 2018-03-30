package spaceInvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameEnvironment extends JPanel implements Runnable, Commons {

    private Dimension d;
    private ArrayList<EnemyShips> enemyShips;
    private Player player;
    private Shot shot;

    private final int enemyShipStartX = 150;
    private final int enemyShipStartY = 5;
    private int direction = -1;
    private int deaths = 0;
    public static int speedOfGame = 15;

    private boolean inGame = true;
    private String message = "Game Over";

    // Thread used to animate/draw objects
    private Thread animationThread;
    
    // Lock used to pause and resume game
    private final Object pauseLock = new Object();
    
    // Defining paused as a volatile variable because it doens't have the chance to ever be blocked.
    volatile static boolean paused = true;

    public GameEnvironment() {
    	
    }

    protected void createGameEnvironment() {

    	// adding input key listeners to current panel
        addKeyListener(new KeyWatcher());
        
        // Setting focus of input onto the current panel
        setFocusable(true);
        
        // Create the dimensions for the board (derived from interface)
        d = new Dimension(gameFrameWidth, gameFrameHeight);
        
        // Setting background color (derived from interface)
        setBackground(backgroundColor);

        // Start the game
        initializeGame();
        
        // This component is used to paint so helps if there is an offscreen painting buffer (improved performance I believe)
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        initializeGame();
    }

    public void initializeGame() {

    	// Create enemy ships
        enemyShips = new ArrayList<>();
        
        for (int i = 0; i < numberOfRowsEnemyShips; i++) {
            for (int j = 0; j < numberOfColumnsEnemyShips; j++) {
            	// increment their starting point so they don't collide overlap with each other
                EnemyShips enemyShip = new EnemyShips(enemyShipStartX + j* 18, enemyShipStartY + i* 18);
                enemyShips.add(enemyShip);
            }
        }
        player = Player.getInstance();
        shot = new Shot();
        
        // If it hasn't been initialized yet, then initialize it.
        if (animationThread == null || !inGame) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void drawEnemyShips(Graphics g) {
        for (EnemyShips enemyShip: enemyShips) {
            if (enemyShip.isVisible()) {
                g.drawImage(enemyShip.getImage(), enemyShip.getX(), enemyShip.getY(), this);
            }
            if (enemyShip.isDying()) {
                enemyShip.isDead();
            }
        }
    }

    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.isDead();
            inGame = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {
        for (EnemyShips a : enemyShips) {
            EnemyShot b = a.getCurrentShot();
            if (!b.isDestroyed()) { 
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);
        if (inGame) {
            g.drawLine(0, playerEventHorizon, gameFrameWidth, playerEventHorizon);
            drawEnemyShips(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        }
        Toolkit.getDefaultToolkit().sync();
        // g.dispose();
    }

    public void gameOver() {

    	// Print game over message
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, gameFrameWidth, gameFrameHeight);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, gameFrameWidth / 2 - 30, gameFrameWidth - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, gameFrameWidth / 2 - 30, gameFrameWidth - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (gameFrameWidth - metr.stringWidth(message)) / 2,
                gameFrameWidth / 2);
    }

    public void animationCycle() {

    	
    	
        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {

            inGame = false;
            message = "Game won!";
        }

        // player
        player.move();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();
            int y;

            for (EnemyShips alien: enemyShips) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + enemyShipIconWidth)
                            && shotY >= (alienY)
                            && shotY <= (alienY + enemyShipIconHeight)) {
                        alien.setDying(true);
                        deaths++;
                        shot.isDead();
                    }
                }
            }

            y = shot.getY()-4;

            if (y < 0) {
                shot.isDead();
            } else {
                shot.setY(y);
            }
        }

        // aliens

        for (EnemyShips alien: enemyShips) {

            int x = alien.getX();

            if (x >= gameFrameWidth - BORDER_RIGHT && direction != -1) {

                direction = -1;
                Iterator i1 = enemyShips.iterator();

                while (i1.hasNext()) {

                    EnemyShips a2 = (EnemyShips) i1.next();
                    a2.setY(a2.getY() + advanceEnemyShipsRate);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator i2 = enemyShips.iterator();

                while (i2.hasNext()) {

                    EnemyShips a = (EnemyShips) i2.next();
                    a.setY(a.getY() + advanceEnemyShipsRate);
                }
            }
        }

        Iterator it = enemyShips.iterator();

        while (it.hasNext()) {
            EnemyShips alien = (EnemyShips) it.next();
            if (alien.isVisible()) {
                int y = alien.getY();
                if (y > playerEventHorizon - enemyShipIconHeight) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.move(direction);
            }
        }

        // bombs
        Random generator = new Random();
        int enemyShotX,enemyShotY;
        int playerX, playerY;
        int shot;
        
        for (EnemyShips alien: enemyShips) {
            shot = generator.nextInt(15);
            EnemyShot b = alien.getCurrentShot();
            if (shot == probabilityOfShot && alien.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
                b.setX(alien.getX());
                b.setY(alien.getY());
            }
            enemyShotX = b.getX();
            enemyShotY = b.getY();
            playerX = player.getX();
            playerY = player.getY();
            if (player.isVisible() && !b.isDestroyed()) {
                if (enemyShotX >= (playerX) && enemyShotX <= (playerX + playerIconWidth) && enemyShotY >= (playerY) && enemyShotY <= (playerY + playerIconHeight)) {
                    player.setDying(true);
                    b.setDestroyed(true);
                }
            }
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);
                if (b.getY() >= playerEventHorizon - shotSize) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    @Override
    public void run() {

        long startTime, timeDiff, sleepTime;

        startTime = System.currentTimeMillis();
        
        // Main Game Loop
        while (inGame) {
        	synchronized(pauseLock) {
        		
        		// If Game over then exit game
        		if(!inGame)
        			break;
        		
        		// If the game is paused then wait till lock is removed
        		if(paused) {
        			try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please restart the game");
					}
        			
        			// Game could be exitted while in pause state. This should be accounted for
        			if(!inGame) {
        				break;
        			}
        		}
        		
        		// call paintComponent again
        		repaint();
        		// do the animation cycle
        		animationCycle();

        		timeDiff = System.currentTimeMillis() - startTime;
        		// Adjust speed of game/thread by taking time difference and setting sleep to a lower level
        		sleepTime = speedOfGame - timeDiff;
        		
        		// Threshold speed level 
        		if (sleepTime < 0) {
        			sleepTime = 2;
        		}

        		try {
        			Thread.sleep(sleepTime);
        		} catch (InterruptedException e) {
        			System.out.println("interrupted");
        		}
        		startTime = System.currentTimeMillis();
        	}
        }
        gameOver();
    }
    
    // This method stops the game thread
    public void stopGame() {
        inGame = false;
        resumeGame();
    }

    // This method pauses the game thread
    public void pauseGame() {
        paused = true;
    }

    public void resumeGame() {
        synchronized (pauseLock) {
        	
        	// Set paused state variable
            paused = false;
            
            // this will unblock the thread
            pauseLock.notifyAll();
        }
    }

    private class KeyWatcher extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();

            if ( e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
            if( e.getKeyCode() == KeyEvent.VK_P) {
            	pauseGame();
            }
            if( e.getKeyCode() == KeyEvent.VK_R) {
            	resumeGame();
            }
        }
    }
}