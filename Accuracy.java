import bagel.*;

/**
 * Class for dealing with accuracy of pressing the notes
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 *  @author Stella Li
 *
 *  Updated by Rama Kaorma for SWEN20003 Project 2B
 */

public class Accuracy {
    /**
     * Scores for key presses
     */
    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    /**
     * Messages for key prsses
     */
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    /**
     * Messages for special key presses
     */
    public static final String DOUBLE = "Double Score";
    public static final String SPEED = "Speed Up";
    public static final String SLOW = "Slow Down";
    public static final String BOMB = "Lane Clear";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private static final int DOUBLE_SCORE_FRAMES = 480;
    private static int growthFactor = 1;
    private int doubleScoreFrameCount = 0;
    private String currAccuracy = null;
    private int frameCount = 0;

    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Evaluate the score of a key press for normal and hold notes
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE * growthFactor;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE * growthFactor;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE * growthFactor;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE * growthFactor;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE * growthFactor;
        }

        return NOT_SCORED;

    }

    /**
     * Evaluate the score of a key press for special notes
     */
    public int evaluateSpecialScore(int height, int targetHeight, boolean triggered, String noteType) {
        int distance = Math.abs(height - targetHeight);
        if (triggered && distance <= GOOD_RADIUS) {
            switch (noteType) {
                case SpecialNote.DOUBLE_SCORE:
                    // Double scores
                    upgradeGrowthFactor(2);
                    setAccuracy(Accuracy.DOUBLE);
                    break;
                case SpecialNote.SPEED_UP:
                    // Increase speed
                    setAccuracy(Accuracy.SPEED);
                    return SpecialNote.SPEED_UP_BONUS * growthFactor;
                case SpecialNote.SLOW_DOWN:
                    // Decrease speed
                    setAccuracy(Accuracy.SLOW);
                    return SpecialNote.SPEED_UP_BONUS * growthFactor;
                case SpecialNote.BOMB:
                    // Clear lane
                    setAccuracy(Accuracy.BOMB);
                    break;
            }
//            return PERFECT_SCORE * growthFactor;
        }

        return NOT_SCORED;

    }

    /**
     * Display messages associated with notes */
    public void update() {
        // Render messages
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }

        // Keep track of double score effect
        doubleScoreFrameCount++;
        if (doubleScoreFrameCount > DOUBLE_SCORE_FRAMES) {
            upgradeGrowthFactor(1);
            doubleScoreFrameCount = 0;
        }

    }

    /**
     * Double score
     */
    public void upgradeGrowthFactor(int factor) {
        growthFactor *= factor;
    }
}
