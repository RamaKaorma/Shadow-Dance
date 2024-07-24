import bagel.Image;

import java.awt.*;
import java.util.Random;

/**
 * Class for enemies extends Character class
 * @author Rama Kaorma
 */

public class Enemy extends Character {

    private final static String NAME = "enemy";

    private final static int EAST = 1;
    private final static int WEST = -1;
    private boolean active = true;
    private int movementDirection;

    public Enemy(Point position) {
        super(NAME, position);

        // Next 3 lines written by ChatGPT, comments by Rama
        Random random = new Random();
        int direction = random.nextInt(2); // Random integer 0 or 1
        this.movementDirection = (direction == 0) ? WEST : EAST; // Set 0 to -1 (West) and 1 remains 1 (East)
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void update() {
        if (active) {
            setPosition(getPosition().x + movementDirection, getPosition().y);
            super.update();
        }
    }

    /**
     * Reverse direction of Enemy when it reaches an edge
     */
    public void reverseDirection() {
        if (movementDirection == EAST) {
            movementDirection = WEST;
        } else {
            movementDirection = EAST;
        }
    };
}
