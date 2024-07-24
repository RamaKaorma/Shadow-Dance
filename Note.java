import bagel.*;

/**
 * super Class for notes
 *
 *  Updated by Rama Kaorma for SWEN20003 Project 2B
 */
public abstract class Note {
    private final Image image;
    private final int appearanceFrame;
    private static int speed = 4;
    private int y;
    private boolean active = false;
    private boolean completed = false;
    public Note(int appearanceFrame, int y, String noteType) {
        this.y = y;
        this.appearanceFrame = appearanceFrame;
        image = new Image("res/" + noteType + ".png");
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {return completed;}

    public void deactivate() {
        active = false;
        completed = true;
    }

    public void update() {
        if (active) {
            y += speed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    public int getY() {
        return this.y;
    }

    public int getSpeed() {
        return this.speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public abstract int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey);
}
