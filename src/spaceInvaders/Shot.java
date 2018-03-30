package spaceInvaders;

import javax.swing.ImageIcon;

public class Shot extends Models {

    private final String shotImg = "src/shot.png";
    private final int horizontalSpace = 6;
    private final int verticalSpace = 1;
 
    public Shot() {
    	// Sometimes we need to create a shot without initializing it first, so default constructor needed
    }

    public Shot(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {

        ImageIcon shotIcon = new ImageIcon(shotImg);
        setIcon(shotIcon.getImage());
        
        setX(x + horizontalSpace);
        setY(y - verticalSpace);
    }
}