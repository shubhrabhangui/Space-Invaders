package spaceInvaders;

import javax.swing.ImageIcon;

	public class EnemyShot extends Models {

        private final String bombImg = "src/enemyShot.png";
        private boolean isDestroyed;

        public EnemyShot(int x, int y) {
            setShot(x, y);
        }

        private void setShot(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(bombImg);
            setIcon(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.isDestroyed = destroyed;
        }

        public boolean isDestroyed() {
            return isDestroyed;
        }
    }
