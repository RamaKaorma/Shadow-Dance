import bagel.*;

/**
 * Class for special notes
 * Adapted from solution by Stella Li
 */
public class SpecialNote extends Note {
    public static final int SPEED_UP_BONUS = 15; /** Bonus for scoring */
    private static final int DROP_HEIGHT = 100;
    private static final String SPECIAL_NOTE = "note";
    /**
     * Filenames for special notes
     */
    public static final String DOUBLE_SCORE = "2x";
    public static final String SPEED_UP = "SpeedUp";
    public static final String SLOW_DOWN = "SlowDown";
    public static final String BOMB = "Bomb";

    private final String type;
    private boolean noteOn = false;

    public SpecialNote(String dir, String type, int appearanceFrame) {
        super(appearanceFrame, DROP_HEIGHT, SPECIAL_NOTE + type);
        this.type = type;
    }

    /**
     * Activate a special note
     */
    public void turnOn() {
        switch (type) {
            case DOUBLE_SCORE:
            case BOMB:
                noteOn = true;
                break;
            case SPEED_UP:
                noteOn = true;
                super.setSpeed(super.getSpeed() + 1);
                break;
            case SLOW_DOWN:
                noteOn = true;
                super.setSpeed(super.getSpeed() - 1);
                break;
        }
    }

    /**
     * Scored once and activated if within range
     * @param input User input (key presses)
     * @param accuracy Score associated with note
     * @param targetHeight Target point of note
     * @param relevantKey Key associated with the note
     * @return Score of the note
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(super.getY(), targetHeight, input.wasPressed(relevantKey), type);
            if (super.getY() > targetHeight) {
                deactivate();
                return 0;
            }
            if (type.equals(BOMB)) {
                turnOn();
                return 0;
            }
            if (score != Accuracy.NOT_SCORED) {
                turnOn();
                deactivate();
                return score;
            }
        }
        return 0;
    }

    public String getType() {
        return type;
    }

    public boolean isNoteOn() {
        return noteOn;
    }
}
