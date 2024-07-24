import bagel.*;

/**
 * Class for the lanes which notes fall down
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 *  @author Stella Li
 *
 *  Updated by Rama Kaorma for SWEN20003 Project 2B
 */

public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private final NormalNote[] normalNotes = new NormalNote[100];
    private int numNotes = 0;
    private final HoldNote[] holdNotes = new HoldNote[20];
    private int numHoldNotes = 0;
    private final SpecialNote[] specialNotes = new SpecialNote[10];
    private int numSpecialNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private int currHoldNote = 0;
    private int currSpecialNote = 0;

    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
        }
    }

    public String getType() {
        return type;
    }

    /**
     * updates all the notes in the lane
     * @param input User input (key presses)
     * @param accuracy Score of note
     * @return score of a note
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (int i = currNote; i < numNotes; i++) {
            normalNotes[i].update();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].update();
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
            specialNotes[k].update();
        }


        if (currNote < numNotes) {
            int score = normalNotes[currNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (normalNotes[currNote].isCompleted()) {
                currNote++;
                return score;
            }
        }

        if (currHoldNote < numHoldNotes) {
            int score = holdNotes[currHoldNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (holdNotes[currHoldNote].isCompleted()) {
                currHoldNote++;
            }
            return score;
        }

        if (currSpecialNote < numSpecialNotes) {
            int score = specialNotes[currSpecialNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (specialNotes[currSpecialNote].isCompleted()) {
                if (specialNotes[currSpecialNote].getType().equals("Bomb") &&
                        specialNotes[currSpecialNote].isNoteOn()) {
                    reset();
                }
                currSpecialNote++;
                return score;
            }
        }

        return Accuracy.NOT_SCORED;
    }

    public void addNote(NormalNote n) {
        normalNotes[numNotes++] = n;
    }

    public void addHoldNote(HoldNote hn) {
        holdNotes[numHoldNotes++] = hn;
    }

    public void addSpecialNote(SpecialNote sn) {
        specialNotes[numSpecialNotes++] = sn;
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!normalNotes[i].isCompleted()) {
                return false;
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if (!holdNotes[j].isCompleted()) {
                return false;
            }
        }

        for (int k = 0; k < numSpecialNotes; k++) {
            if (!specialNotes[k].isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < numNotes; i++) {
            normalNotes[i].draw(location);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].draw(location);
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
            specialNotes[k].draw(location);
        }
    }

    /**
     * Clear lane of active notes
     */

    public void reset() {
        for (int i = currNote; i < numNotes; i++) {
            if (normalNotes[i].isActive()) {
                normalNotes[i].deactivate();
            }
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            if (holdNotes[j].isActive()) {
                holdNotes[j].deactivate();
            }
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
            if (specialNotes[k].isActive()) {
                specialNotes[k].deactivate();
            }
        }
    }

    public int getNumNotes() {
        return numNotes;
    }

    public int getLocation() {
        return location;
    }

    public NormalNote[] getNormalNotes() {
        return normalNotes;
    }
}
