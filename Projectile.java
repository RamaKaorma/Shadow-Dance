import bagel.DrawOptions;
import bagel.Image;

import java.awt.*;

/**
 * Class for projectiles
 * @author Rama Kaorma
 */

public class Projectile implements Shootable {
    private final static Image PROJECTILE_IMAGE = new Image("res/arrow.png");
    private final static int PROJECTILE_SPEED = 6;
    private Point target;
    private DrawOptions rotation = new DrawOptions();
    private final Point position = new Point(Guardian.GUARDIAN_X, Guardian.GUARDIAN_Y);
    private boolean active = true;

    public Projectile(Point target, double rotation) {
        this.target = target;
        this.rotation.setRotation(rotation);
    }

    /**
     * Refresh projectile to update movement
     */
    @Override
    public void update() {
        if (active) {
            if (getPosition().y <= 0) {
                deactivate();
            }  else {
                draw();
                double distance = Math.sqrt((target.x - position.x) * (target.x - position.x) + (target.y - position.y) * (target.y - position.y));
                double x = (target.x - position.x) / distance;
                double y = (target.y - position.y) / distance;
                position.x += PROJECTILE_SPEED * x;
                position.y += PROJECTILE_SPEED * y;
                if (distance <= Level.CLOSE_CONTACT_RANGE * Level.HALF) {
                    deactivate();
                }
            }
        }
    }

    /**
     * Draw projectile
     */
    public void draw() {
        if (active) {
            PROJECTILE_IMAGE.draw(position.x, position.y, rotation);
        }
    }

    @Override
    public void checkContact() {

    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Point getPosition() {
        return position;
    }

    /**
     * Reset the aim point of projectile to origin
     */
    public void reset() {
        target.x = Guardian.GUARDIAN_X;
        target.y = Guardian.GUARDIAN_Y;
    }
}