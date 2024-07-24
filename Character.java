import bagel.Image;

import java.awt.*;

/**
 * Class for inheritance for guardian and enemy
 * @author Rama Kaorma
 */
public abstract class Character {
    private final Image image;
    private Point position;

    public Character(String name, Point position) {
        this.position = position;
        image = new Image("res/" + name + ".png");
    }

    public void update() {
        image.draw(position.x, position.y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }
}
