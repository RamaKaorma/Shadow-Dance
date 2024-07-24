import java.awt.*;

/**
 * Class for guardian extends Character
 * @author Rama Kaorma
 */

public class Guardian extends Character {
    private final static String NAME = "guardian";
    public final static int GUARDIAN_X = 800; /** X-coordinate of stationary guardian */
    public final static int GUARDIAN_Y = 600; /** Y-coordinate of stationary guardian */
    public final static int AMMO = 1000; /** Capacity of Guardian's bag of ammo */
    public final static Point POSITION = new Point(GUARDIAN_X, GUARDIAN_Y); /** Guardian's stationary position */
    public final static int FULL_ANGLE = 180; /** Straight angle for projectile rotation*/
    public Projectile[] projectiles = new Projectile[AMMO]; /** Array of all projectiles shot */
    private int projectileCount = 0;


    public Guardian() {
        super(NAME, POSITION);
    }

    /**
     * Find nearest enemy to shoot using the greedy algorithm
     * @param enemies Array of enemies to inspect
     * @return The enemy closest to Guardian
     */
    public Point findNearestEnemy(Enemy[] enemies) {
        Point coordinates = new Point(GUARDIAN_X, GUARDIAN_Y);
        double nearestEnemy = 100000000;

        // Check all enemies
        for (Enemy enemy : enemies) {
            if (enemy == null) {
                return coordinates;
            }
            if (!enemy.isActive()) {
                continue;
            }

            int enemyX = enemy.getPosition().x;
            int enemyY = enemy.getPosition().y;
            double curEnemy;

            // Distance between guardian and enemy
            curEnemy = Math.abs(Math.sqrt(
                    (enemyX - GUARDIAN_X) * (enemyX - GUARDIAN_X) +
                            (enemyY - GUARDIAN_Y) * (enemyY - GUARDIAN_Y)));

            if (curEnemy < nearestEnemy) {
                nearestEnemy = curEnemy;
                coordinates = enemy.getPosition();
            }
        }
        return coordinates;
    }

    /**
     * Shoot a projectile at the nearest enemy
     * @param enemies Array of enemies to shoot at
     */
    public void shoot(Enemy[] enemies) {
        Point nearestEnemy = findNearestEnemy(enemies);
        double angle = (double) (nearestEnemy.y - GUARDIAN_Y) / (nearestEnemy.x - GUARDIAN_X);
        double rotation = FULL_ANGLE - Math.atan(angle);
//        System.out.println(rotation);
        Projectile arrow = new Projectile(nearestEnemy, rotation);
        try {
            projectiles[projectileCount++] = arrow;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Out of ammo. Guardian is dead");
        }
    }

    public int getProjectileCount() {
        return projectileCount;
    }
}
