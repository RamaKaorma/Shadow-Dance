import java.awt.*;
import java.util.Random;

/**
 * Class for storing data relevant to the level
 * @author Rama Kaorma
 */

public class Level {
    public final static int CLOSE_CONTACT_RANGE = 104;
    private final static int LEVEL_ONE_TARGET = 150;
    private final static int LEVEL_TWO_TARGET = 400;
    private final static int LEVEL_THREE_TARGET = 350;
    private final static int NORMAL_LANE_LIMIT = 4;
    private final static int RIGHT_MOVEMENT_BOUNDARY = 800;
    private final static int BOTTOM_MOVEMENT_BOUNDARY = 500;
    private final static int BOUNDARY_OFFSET = 100;
    private final static int SQUARED = 2;
    public final static double HALF = 0.5;
    /*
     Assumption that there's an army of enemies consisting of only 100 :)
     A game in which all enemies are released would go on for ~8 minutes.
     */
    private final static int ENEMY_LIMIT = 100;
    private int levelNumber;
    private int clearScore;
    private int numOfLanes;
    public Guardian guardian;
    private final String CSVFile;
    public Enemy[] enemies = null;
    private int enemyCount = 0;

    /**
     * Determine level specifications
     * @param levelNumber Number of level to play
     */
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        if (levelNumber == ShadowDance.LEVEL_ONE) {
            this.clearScore = LEVEL_ONE_TARGET;
        } else if (levelNumber == ShadowDance.LEVEL_TWO) {
            this.clearScore = LEVEL_TWO_TARGET;
        } else if (levelNumber == ShadowDance.LEVEL_THREE) {
            this.clearScore = LEVEL_THREE_TARGET;
            this.guardian = new Guardian();
            this.enemies = new Enemy[ENEMY_LIMIT];
        }
        this.numOfLanes = NORMAL_LANE_LIMIT;
        this.CSVFile = "res/level" + levelNumber + ".csv";
    }

    public int getClearScore() {
        return clearScore;
    }

    public String getCSVFile() {
        return CSVFile;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    /**
     * New enemy on screen
     */
    public void addEnemy() {
        Random random = new Random();
        int randomX = random.nextInt(RIGHT_MOVEMENT_BOUNDARY+1) + BOUNDARY_OFFSET;
        int randomY = random.nextInt(BOTTOM_MOVEMENT_BOUNDARY+1) + BOUNDARY_OFFSET;
        Point position = new Point(randomX, randomY);
        enemies[enemyCount++] = new Enemy(position);
    }

    /**
     * Update enemy movement and contact with arrows
     */
    public void updateEnemies() {
        for (int i=0; i<enemyCount; i++) {
            if (!enemies[i].isActive()) {
                continue;
            }
            enemies[i].update();

            if (enemies[i].getPosition().x <= BOUNDARY_OFFSET ||
                    enemies[i].getPosition().x >= RIGHT_MOVEMENT_BOUNDARY+BOUNDARY_OFFSET) {
                enemies[i].reverseDirection();
            }

            for (int j=0; j<guardian.getProjectileCount(); j++) {
                Projectile arrow = guardian.projectiles[j];
                if (!arrow.isActive()) {
                    continue;
                }

                double distance = Math.abs(Math.sqrt(
                        Math.pow(arrow.getPosition().x - enemies[i].getPosition().x, SQUARED) +
                                Math.pow(arrow.getPosition().y - enemies[i].getPosition().y, SQUARED)));
                if (distance <= CLOSE_CONTACT_RANGE * HALF) {
                    if (!enemies[i].isActive()) {
                        arrow.reset();
                    }
                    enemies[i].deactivate();

                }
            }
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    /**
     * Check Enemy contact with notes
     * @param notes Array of notes
     * @param x coordinate of the x contact point
     */
    public void checkContact(Note[] notes, int x) {
        for (Note note : notes) {
            if (note != null) {
                for (int j = 0; j < enemyCount; j++) {
                    double distance = Math.abs(Math.sqrt(
                            (x - enemies[j].getPosition().x) * (x - enemies[j].getPosition().x) +
                                    (note.getY() - enemies[j].getPosition().y) *
                                    (note.getY() - enemies[j].getPosition().y)));

                    if (distance <= CLOSE_CONTACT_RANGE && note.isActive() && enemies[j].isActive()) {
                        note.deactivate();
                    }
                }
            } else {
                return;
            }
        }
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    /**
     * Reset level for new game
     */
    public void reset() {
        enemies = null;
        enemyCount = 0;
    }
}
