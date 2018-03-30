package spaceInvaders;

import javax.swing.ImageIcon;

public class EnemyShips extends Models {

    private EnemyShot enemyShot;
    private ImageIcon enemyIcon = new ImageIcon("src/enemyShip.png");

    public EnemyShips(int x, int y) {
        setShip(x, y);
    }

    private void setShip(int x, int y) {

    	// set coordinates
        this.x = x;
        this.y = y;
        
        // Create a shot at specified coordinate
        enemyShot = new EnemyShot(x, y);
        setIcon(enemyIcon.getImage());
    }

    public void move(int direction) {
        this.x += direction;
    }

    public EnemyShot getCurrentShot() {
        return enemyShot;
    }
}
