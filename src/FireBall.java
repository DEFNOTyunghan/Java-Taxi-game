import bagel.*;
import java.util.Properties;

/**
 * Represents a fireball that can be rendered on the screen.
 * The FireBall class manages its fireball properties.
 */
public class FireBall {
    private final Properties GAME_PROPS;

    private final Image FIRE_BALL_IMAGE;
    private final int FIRE_BALL_SPEED;
    private final double FIRE_BALL_RADIUS;
    private final int SCREEN_SPEED;
    private final double FIRE_BALL_DAMAGE;

    private double XPos;
    private double YPos;

    private boolean hasCollided = false;

    /**
     * Constructs a FireBall object with specified properties.
     *
     * @param gameProps the game properties containing image paths, speed, and other settings
     * @param XPos the initial X position of the fireball
     * @param YPos the initial Y position of the fireball
     */
    public FireBall(Properties gameProps, double XPos, double YPos) {
        this.GAME_PROPS = gameProps;

        FIRE_BALL_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.fireball.image"));
        FIRE_BALL_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.fireball.shootSpeedY"));
        FIRE_BALL_RADIUS = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.fireball.radius"));
        SCREEN_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY"));
        FIRE_BALL_DAMAGE = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.fireball.damage"));

        this.XPos = XPos;
        this.YPos = YPos;
    }

    /**
     * Renders the fireball on the screen and updates its position based on user input.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        FIRE_BALL_IMAGE.draw(XPos, YPos);

        YPos -= FIRE_BALL_SPEED;

        if (input.isDown(Keys.UP) && driverInTaxi) {
            YPos += SCREEN_SPEED;
        } else if (input.isDown(Keys.UP) && !driverInTaxi) {
            YPos += 1;
        } else if (input.isDown(Keys.DOWN) && !driverInTaxi) {
            YPos -= 1;
        }

    }

    // getters and setters
    public double getXPos() {
        return XPos;
    }

    public double getYPos() {
        return YPos;
    }

    public double getFIRE_BALL_RADIUS() {
        return FIRE_BALL_RADIUS;
    }

    public double getFIRE_BALL_DAMAGE() {
        return FIRE_BALL_DAMAGE;
    }

    public boolean HasCollided() {
        return hasCollided;
    }

    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }
}
