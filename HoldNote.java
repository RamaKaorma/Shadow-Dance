import bagel.*;

/**
 * Class for hold notes
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 *  @author Stella Li
 *
 *  Updated by Rama Kaorma for SWEN20003 Project 2B
 */

public class HoldNote extends Note {
    private static final String HOLD_NOTE = "holdNote";
    private static final int HEIGHT_OFFSET = 82;
    private static final int DROP_HEIGHT = 24;
    private boolean holdStarted = false;

    public HoldNote(String lane, int appearanceFrame) {
        super(appearanceFrame, DROP_HEIGHT, HOLD_NOTE + lane);
    }

    public void startHold() {
        holdStarted = true;
    }



    /**
     * Scored twice, once at the start of the hold and once at the end
     * @param input User input (Key presses)
     * @param accuracy Score for note
     * @param targetHeight Target point for note activation
     * @param relevantKey Key associated with note
     * @return Score of note
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return super.getY() + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return super.getY() - HEIGHT_OFFSET;
    }
}
