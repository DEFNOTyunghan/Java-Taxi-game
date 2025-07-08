import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Represents a star power-up in the game that grants invincibility to the taxi.
 * The star can be collected by the taxi, and it has properties such as position
 * and collision detection.
 */
public class Star extends PowerUp{
    private final Image STAR_IMAGE;

    /**
     * Constructs a Star object with specified properties and initial position.
     *
     * @param gameProps the game properties containing image and speed settings
     * @param starX the initial X coordinate of the star
     * @param starY the initial Y coordinate of the star
     */
    public Star(Properties gameProps, double starX, double starY) {
        // set coin properties
        STAR_IMAGE = new Image(gameProps.getProperty("gameObjects.invinciblePower.image"));
        setXPos(starX);
        setYPos(starY);
        setSCREEN_SPEED(Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY")));
        setTAXI_RADIUS(gameProps.getProperty("gameObjects.taxi.radius"));
        setPOWER_UP_RADIUS(gameProps.getProperty("gameObjects.coin.radius"));
    }

    /**
     * Renders the star on the screen if it hasn't collided and updates its position
     * based on user input.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        if (!isHasCollided()) {
            STAR_IMAGE.draw(getXPos(), getYPos());
        }
        if (input.isDown(Keys.UP) && driverInTaxi) {
            setYPos(getYPos() + getSCREEN_SPEED());
        } else if (input.isDown(Keys.UP) && !driverInTaxi) {
            setYPos(getYPos() + 1);
        } else if (input.isDown(Keys.DOWN) && !driverInTaxi) {
            setYPos(getYPos() - 1);
        }
    }
}
