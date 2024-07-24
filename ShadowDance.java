import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 * @author Stella Li
 *
 * Updated by Rama Kaorma for SWEN20003 Project 2B
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    public final static int LEVEL_ONE = 1; /** Level 1 identifier */
    public final static int LEVEL_TWO = 2; /** Level 2 identifier */
    public final static int LEVEL_THREE = 3; /** Level 3 identifier */
    public final static String FONT_FILE = "res/FSO8BITR.TTF"; /** Text font */
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS = "Select Levels With\nNumber Keys\n\n     1      2      3";
    private static final int CLEAR_SCORE = 150;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String END_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static final String DOUBLE_NOTE_SHORTCUT = "2x";
    private Accuracy accuracy = new Accuracy();
    private Lane[] lanes = new Lane[4];
    private Lane specialLane;
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private Track track = new Track("res/track1.wav");
    private boolean started = false;
    private boolean finished = false;
    private boolean paused = false;
    private Level level;
    private int enemyFrameCount = 0;
    private final static int ENEMY_APPEARANCE_CYCLE = 200;

    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        // readCsv();
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }


    /**
     * Read world file and process contents
     */
    private void readCsv(String CSVFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSVFile))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");
                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                NormalNote normalNote = new NormalNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(normalNote);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                            default:
                                SpecialNote specialNote;
                                if (splitText[1].equals("DoubleScore")) {
                                    specialNote = new SpecialNote(dir, DOUBLE_NOTE_SHORTCUT, Integer.parseInt(splitText[2]));
                                    lane.addSpecialNote(specialNote);
                                } else {
                                    specialNote = new SpecialNote(dir, splitText[1], Integer.parseInt(splitText[2]));
                                    lane.addSpecialNote(specialNote);
                                }

                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

            if (input.wasPressed(Keys.NUM_1)) {
                level = new Level(LEVEL_ONE);
                started = true;
                readCsv(level.getCSVFile());

            } else if (input.wasPressed(Keys.NUM_2)) {
                level = new Level(LEVEL_TWO);
                started = true;
                readCsv(level.getCSVFile());

            } else if (input.wasPressed(Keys.NUM_3)) {
                level = new Level(LEVEL_THREE);
                started = true;
                readCsv(level.getCSVFile());
            }

        } else if (finished) {
            // end screen
            if (score >= level.getClearScore()) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            }
            INSTRUCTION_FONT.drawString(END_MESSAGE,
                        WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(END_MESSAGE)/2,
                        WINDOW_HEIGHT/2+100);

            if (input.wasPressed(Keys.SPACE)) {
                resetGame();
            }

        } else {
            // gameplay

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
//                    track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {
                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].update(input, accuracy);
                }

                accuracy.update();

                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
//                    track.pause();
                }
                if (level.getLevelNumber() == LEVEL_THREE) {
                    level.getGuardian().update();
                    level.updateEnemies();

                    if (currFrame % ENEMY_APPEARANCE_CYCLE == 0) {
                        level.addEnemy();
                    }

                    for (int i=0; i<numLanes; i++) {
                        for (int j=0; j<lanes[i].getNumNotes(); j++) {
                            level.checkContact(lanes[i].getNormalNotes(), lanes[i].getLocation());
                        }
                    }

                    if (input.wasPressed(Keys.LEFT_SHIFT)) {
                        level.guardian.shoot(level.getEnemies());
                    }

                    for (int i=0; i<level.guardian.getProjectileCount(); i++) {
                        Projectile arrow = level.guardian.projectiles[i];
                        arrow.update();
                    }
                }
            }
        }

    }

    public static int getCurrFrame() {
        return currFrame;
    }

    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Restart game
     */
    private void resetGame() {
        for (int i=0; i<numLanes; i++) {
            lanes[i].reset();
        }
        lanes = new Lane[4];
        accuracy = new Accuracy();
        numLanes = 0;
        score = 0;
        currFrame = 0;
        started = false;
        paused = false;
        finished = false;
        level.reset();
    }
}
