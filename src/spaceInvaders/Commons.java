package spaceInvaders;

import java.awt.Color;

public interface Commons {
	
	public static final Color backgroundColor = Color.black;
	
	// Enemy Ship related parameters
	public static final int numberOfRowsEnemyShips = 4;
	public static final int numberOfColumnsEnemyShips = 6;
    public static final int enemyShipIconHeight = 12;
    public static final int enemyShipIconWidth = 12;
    public static final int advanceEnemyShipsRate = 15;
	
	// Game Frame related parameters
    public static final int gameFrameWidth = 600;
    public static final int gameFrameHeight = 600;
    public static final int playerEventHorizon = 550;
    public static final int BORDER_RIGHT = 30;
    public static final int BORDER_LEFT = 5;
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    
    // Shot related parameters
    public static final int shotSize = 5;
    public static final int probabilityOfShot = 5;
    
    // Player ship related parameters
    public static final int playerIconHeight = 10;
    public static final int playerIconWidth = 15;

}
