import bagel.*;

/**
 * Class for normal notes
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 *  @author Stella Li
 *
 *  Updated by Rama Kaorma for SWEN20003 Project 2B
 */
public class NormalNote extends Note {
    private static final String NORMAL_NOTE = "note";
    private static final int DROP_HEIGHT = 100;
    public NormalNote(String lane, int appearanceFrame) {
        super(appearanceFrame, DROP_HEIGHT, NORMAL_NOTE+lane);
    }

    /**
     *
     */

    /**
     * Scoring for normal notes
     * @param input User input (key presses)
     * @param accuracy Score associated with note
     * @param targetHeight Target point of note
     * @param relevantKey Key associated with the note
     * @return Score of the note
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(super.getY(), targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }

        }

        return 0;
    }

}
